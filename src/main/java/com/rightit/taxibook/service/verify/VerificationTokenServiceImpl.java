package com.rightit.taxibook.service.verify;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Validator;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.domain.VerificationToken.VerificationTokenType;
import com.rightit.taxibook.repository.UseRepository;
import com.rightit.taxibook.repository.VerificationTokenRepository;
import com.rightit.taxibook.service.AbstractService;
import com.rightit.taxibook.service.mail.EmailMessage;
import com.rightit.taxibook.service.mail.EmailService;
import com.rightit.taxibook.service.password.PasswordHashService;
import com.rightit.taxibook.spec.Specification;
import com.rightit.taxibook.spec.query.FindByEmailAddressSpec;
import com.rightit.taxibook.spec.query.FindByIdSpec;
import com.rightit.taxibook.spec.query.FindVerificationTokenSpec;
import com.rightit.taxibook.spec.update.UpdateUserPasswordSpec;
import com.rightit.taxibook.spec.update.UpdateVerificationStatusSpec;
import com.rightit.taxibook.template.TemplateMerger;
import com.rightit.taxibook.util.FailedCompletableFutureBuilder;
import com.rightit.taxibook.validation.exception.TokenAlreadyVerifiedException;
import com.rightit.taxibook.validation.exception.TokenHasExpiredException;
import com.rightit.taxibook.validation.exception.TokenNotFoundException;
import com.rightit.taxibook.validation.exception.UserAlreadyVerifiedException;
import com.rightit.taxibook.validation.exception.UserNotFoundException;
import com.rightit.taxibook.validation.exception.UserNotVerifiedException;

public class VerificationTokenServiceImpl extends AbstractService implements VerificationTokenService {

	private Logger LOGGER = LoggerFactory.getLogger(VerificationTokenServiceImpl.class);
	
	private static final int TOKEN_EXPRIRY_IN_DAYS = 1;
	
	@Inject 
	private EmailService emailService;
	
	@Inject 
	private TemplateMerger templateMerger;
	
	@Inject 
	private UseRepository userRepository;
	
	@Inject 
	private VerificationTokenRepository verificationTokenRepository;
	
	@Inject
	private PasswordHashService passwordHashService;
	
	@Inject
	private TokenGenerator tokenGenerator;

	@Inject
	public VerificationTokenServiceImpl(Provider<Validator> validatorProvider) {
		super(validatorProvider.get());
	}

	@Override
	public CompletableFuture<Optional<VerificationToken>> generateEmailVerificationToken(
			final EmailBasedRequest request) {

		validate(request);

		LOGGER.info(String.format("Generating email verification token for email address: %s...",request.getEmailAddress()));
		final CompletableFuture<User> futureUser = fetchUserByEmailAddress(request.getEmailAddress());
		return futureUser.thenCompose(user -> {
			if (user.isVerified()) {
				String errorMessage = String.format(UserAlreadyVerifiedException.ERROR_MESSAGE, user.getEmailAddress());
				LOGGER.error(errorMessage);
				return new FailedCompletableFutureBuilder<Optional<VerificationToken>>().build(new UserAlreadyVerifiedException(user));
			} else {
				return generateVerificationToken(user, VerificationTokenType.EMAIL_VERIFICATION).thenApply(optionalToken -> {
					sendUserVerificationEmailAsync(user, optionalToken);
					return optionalToken;
				});
			}
		});
	}
	
	@Override
	public CompletableFuture<Optional<VerificationToken>> generateResetPasswordToken(EmailBasedRequest request) {
		
		validate(request);
		
		LOGGER.info(String.format("Generating reset password token for email address: %s...",request.getEmailAddress()));
		final CompletableFuture<User> futureUser = fetchUserByEmailAddress(request.getEmailAddress());		
		return futureUser.thenCompose(user -> {
			if(!user.isVerified()) {
				String errorMessage = String.format(UserNotVerifiedException.ERROR_MESSAGE, user.getEmailAddress());
				LOGGER.error(errorMessage);
				return new FailedCompletableFutureBuilder<Optional<VerificationToken>>().build(
						new UserNotVerifiedException(errorMessage));
			}
			return generateVerificationToken(user, VerificationTokenType.RESET_PASSWORD).thenApply(optionalToken -> {
				sendResetPasswordEmailAsync(user, optionalToken);
				return optionalToken;
			}); 
		});
	}
	
	@Override
	public CompletableFuture<Optional<VerificationToken>> resetPassword(PasswordRequest request) {
		
		validate(request);
		
		LOGGER.info(String.format("Resetting password for token %s...", request.getToken()));
		return getToken(request.getToken()).thenCompose(optionalToken -> {
			return getUser(optionalToken.get().getUserId()).thenApply(optionalUser -> {
				updateTokenVerificationStatusAsync(optionalToken);
				resetUserPasswordAsync(optionalUser.get(), request.getPassword());
				return optionalToken;
			});
		});
	}

	@Override
	public CompletableFuture<Optional<VerificationToken>> verifyUser(final TokenVerificationRequest request) {

		validate(request);

		LOGGER.info(String.format("Verifying token %s...", request.getToken()));
		return getToken(request.getToken()).thenCompose(optionalToken -> {
			final CompletableFuture<Optional<User>> futureOptionalUser = getUser(optionalToken.get().getUserId());
			return futureOptionalUser.thenCompose(optionalUser -> {
				User user = optionalUser.get(); 
				if(user.isVerified()) {
					LOGGER.error(String.format(UserAlreadyVerifiedException.ERROR_MESSAGE, user.getId().toString()));
					return new FailedCompletableFutureBuilder<Optional<User>>().build(
							new UserAlreadyVerifiedException(optionalUser.get()));
				}
				return futureOptionalUser;
			}).thenApply(optionalUser -> {
				updateTokenVerificationStatusAsync(optionalToken);
				updateUserVerificationStatusAsync(optionalUser);
				return optionalToken;
			});
		});
	}
	
	private void resetUserPasswordAsync(final User user, final String newPassword) {
		CompletableFuture.runAsync(() -> {
			LOGGER.info("Updating password for user {}...", user.getId().toString());
			final String hashedPassword = passwordHashService.hashPassword(newPassword);
			final CompletableFuture<Optional<User>> futureOptionalUser = userRepository.updateOne(
					new UpdateUserPasswordSpec(user.getId(), hashedPassword));
			futureOptionalUser.thenApply(returnedOptionalUser -> {
				if(returnedOptionalUser.isPresent()) {
					LOGGER.info("Password for user {} updated.", user.getId());
					sendResetPasswordConfirmationEmailAsync(user);
				} else {
					LOGGER.warn("Could not update password for user {}.", user.getId());
				}				
				return returnedOptionalUser;
			});
			
		});
	}
	
	private void updateTokenVerificationStatusAsync(final Optional<VerificationToken> optionalToken) {
		VerificationToken token = optionalToken.get();
		token.setVerified(true);
		CompletableFuture.runAsync(() -> {
			LOGGER.info(String.format("Persisting verified status for token %s...", token.getToken()));
			CompletableFuture<Optional<VerificationToken>> futureOptionalToken = verificationTokenRepository.updateOne(
					createUpdateVerificationStatusSpec(token.getId(), token.isVerified()));
			futureOptionalToken.thenApply(returnedOptionalToken -> {
				if(returnedOptionalToken.isPresent()) {
					LOGGER.info(String.format("Verification status for token %s updated.", token.getToken()));
				} else {
					LOGGER.warn(String.format("Could not update the verification status for token %s.", token.getToken()));
				}				
				return futureOptionalToken;
			});
		});
	}
	
	private void updateUserVerificationStatusAsync(Optional<User> optionalUser) {
		final User user = optionalUser.get();
		user.setVerified(true);
		CompletableFuture.runAsync(() -> {
			LOGGER.info(String.format("Persisting verified status for user %s...", user.getId().toString()));
			CompletableFuture<Optional<User>> futureOptionalUser = userRepository.updateOne(
					createUpdateVerificationStatusSpec(user.getId(), user.isVerified()));
			futureOptionalUser.thenApply(returnedOptionalUser -> {
				if(returnedOptionalUser.isPresent()) {
					LOGGER.info(String.format("Verification status for user %s updated.", user.getId()));
				} else {
					LOGGER.warn(String.format("Could not update the verification status for user %s.", user.getId()));
				}				
				return returnedOptionalUser;
			});
		});
	}
	
	private Specification createUpdateVerificationStatusSpec(ObjectId id, Boolean newValue) {
		return new UpdateVerificationStatusSpec(id, newValue);
	}

	private CompletableFuture<Optional<VerificationToken>> getToken(final String token) {
		LOGGER.info(String.format("Getting token information: %s ...", token));
		final CompletableFuture<Optional<VerificationToken>> futureToken = verificationTokenRepository.findOne(
				new FindVerificationTokenSpec(token));
		return futureToken.thenCompose(optionalToken -> {
			if(!optionalToken.isPresent()) {
				LOGGER.error(String.format(TokenNotFoundException.ERROR_MESSAGE, token));
				return new FailedCompletableFutureBuilder<Optional<VerificationToken>>().build(
						new TokenNotFoundException(token));
			} else {
				final VerificationToken verificationToken = optionalToken.get();
				if(verificationToken.hasExpired()) {
					LOGGER.error(String.format(TokenHasExpiredException.ERROR_MESSAGE, token));
					return new FailedCompletableFutureBuilder<Optional<VerificationToken>>().build(
							new TokenHasExpiredException(verificationToken));
				}
				else if(verificationToken.isVerified()) {
					LOGGER.error(String.format(TokenAlreadyVerifiedException.ERROR_MESSASE, token));
					return new FailedCompletableFutureBuilder<Optional<VerificationToken>>().build(
							new TokenAlreadyVerifiedException(verificationToken));
				}
				return futureToken;
			}
		});
	}
	
	private CompletableFuture<Optional<User>> getUser(final String userId) {
		LOGGER.info(String.format("Getting user with id %s...", userId));
		final CompletableFuture<Optional<User>> futureOptionalUser = userRepository.findOne(new FindByIdSpec(userId));
		return futureOptionalUser.thenCompose(optionalUser -> {
			if(!optionalUser.isPresent()) {
				String errorMessage = String.format("Could not find user for id: %s.", userId);
				LOGGER.error(errorMessage);
				return new FailedCompletableFutureBuilder<Optional<User>>().build(new UserNotFoundException(errorMessage));
			} 
			return futureOptionalUser;
		});
	}
			
	private void sendUserVerificationEmailAsync(User user, Optional<VerificationToken> optionalToken) {
		CompletableFuture.runAsync(() -> {
			LOGGER.info("Sending token verification email to {} ...", user.getEmailAddress());
			final EmailMessage emailMessage = new VerifyEmailMessageBuilder(optionalToken, templateMerger).apply(user);
			// Send a mail with an embedded link that includes the verification token to the user
			emailService.send(emailMessage);
			LOGGER.info("Token verification email sent for email address: {}.", user.getEmailAddress());
		});
	}
	
	private void sendResetPasswordEmailAsync(User user, Optional<VerificationToken> optionalToken) {
		CompletableFuture.runAsync(() -> {
			LOGGER.info(String.format("Sending reset password email to %s ...", user.getEmailAddress()));
			final EmailMessage emailMessage = new ResetPasswordMessageBuilder(optionalToken, templateMerger).apply(user);
			// Send a mail with an embedded link that includes the reset email token to the user
			emailService.send(emailMessage);
			LOGGER.info("Reset password email sent for email address: {}.", user.getEmailAddress());
		});
	}
	
	private void sendResetPasswordConfirmationEmailAsync(User user) {
		CompletableFuture.runAsync(() -> {
			LOGGER.info("Sending reset password confirmation email to {} ...", user.getEmailAddress());
			final EmailMessage emailMessage = new ResetPasswordConfirmationEmailBuilder(templateMerger).apply(user);
			emailService.send(emailMessage);
			LOGGER.info("Reset password confirmation email sent for email address: {}.", user.getEmailAddress());
		});
	}

	private CompletableFuture<User> fetchUserByEmailAddress(String emailAddress) {
		LOGGER.info("Getting user with email address: {}...",emailAddress);
		final CompletableFuture<Optional<User>> futureOptionalUser = userRepository.findOne(new FindByEmailAddressSpec(emailAddress));
		return futureOptionalUser.thenCompose(optionalUser -> {
			CompletableFuture<User> future = new CompletableFuture<>();
			if (!optionalUser.isPresent()) {
				String errorMessage = String.format("Could not find user by email address: %s.", emailAddress);
				LOGGER.error(errorMessage);
				return new FailedCompletableFutureBuilder<User>().build(new UserNotFoundException(errorMessage));
			} else {
				future.complete(optionalUser.get());
			}			
			return future; 
		});
	}
	
	private CompletableFuture<Optional<VerificationToken>> generateVerificationToken(User user, VerificationTokenType tokenType) {
		DateTime expiryDate = new DateTime().plusDays(TOKEN_EXPRIRY_IN_DAYS);
		return tokenGenerator.generateVerificationToken(user, tokenType, expiryDate);
	} 
}
