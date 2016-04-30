package com.rightit.taxibook.service.verify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.domain.VerificationToken.VerificationTokenType;
import com.rightit.taxibook.repository.UseRepository;
import com.rightit.taxibook.repository.VerificationTokenRepository;
import com.rightit.taxibook.service.AbstractService;
import com.rightit.taxibook.service.mail.EmailMessage;
import com.rightit.taxibook.service.mail.EmailMessage.EmailContentType;
import com.rightit.taxibook.service.mail.EmailMessage.EmailMessageBuilder;
import com.rightit.taxibook.service.mail.EmailService;
import com.rightit.taxibook.spec.Specification;
import com.rightit.taxibook.spec.query.FindActiveVerificationTokenSpec;
import com.rightit.taxibook.spec.query.FindByEmailAddressSpec;
import com.rightit.taxibook.spec.query.FindByIdSpec;
import com.rightit.taxibook.spec.query.FindVerificationTokenSpec;
import com.rightit.taxibook.spec.update.UpdateVerificationStatusSpec;
import com.rightit.taxibook.template.MergeException;
import com.rightit.taxibook.template.TemplateMerger;
import com.rightit.taxibook.util.FailedCompletableFutureBuilder;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;
import com.rightit.taxibook.validation.exception.TokenAlreadyVerifiedException;
import com.rightit.taxibook.validation.exception.TokenHasExpiredException;
import com.rightit.taxibook.validation.exception.TokenNotFoundException;
import com.rightit.taxibook.validation.exception.UserAlreadyVerifiedException;
import com.rightit.taxibook.validation.exception.UserNotFoundException;

public class VerificationTokenServiceImpl extends AbstractService implements VerificationTokenService {

	private Logger logger = Logger.getLogger(VerificationTokenServiceImpl.class);
	@Inject 
	private EmailService emailService;
	@Inject 
	private TemplateMerger templateMerger;
	@Inject 
	private UseRepository userRepostory;
	@Inject 
	private VerificationTokenRepository verificationTokenRepository;

	@Inject
	public VerificationTokenServiceImpl(Provider<Validator> validatorProvider) {
		super(validatorProvider.get());
	}

	@Override
	public CompletableFuture<Optional<VerificationToken>> generateEmailVerificationToken(
			final EmailVerificationRequest request) {

		validate(request);

		logger.info(String.format("Generating email verification token for email address: %s...",request.getEmailAddress()));
		final CompletableFuture<User> futureUser = fetchUserByEmailAddress(request.getEmailAddress());
		return futureUser.thenCompose(user -> {
			if (user.isVerified()) {
				logger.error(String.format("User with id \"%s\" is already verified.", user.getId().toString()));
				return new FailedCompletableFutureBuilder<Optional<VerificationToken>>().build(new UserAlreadyVerifiedException(user));
			} else {
				return generateVerificationToken(user).thenApply(optionalToken -> {
					sendTokenVerificationEmailAsync(user, optionalToken);
					return optionalToken;
				});
			}
		});
	}

	@Override
	public CompletableFuture<Optional<VerificationToken>> verify(final TokenVerificationRequest request) {

		validate(request);

		logger.info(String.format("Verifying token %s...", request.getToken()));
		final CompletableFuture<Optional<VerificationToken>> futureOptionalToken = getToken(request.getToken());
		return futureOptionalToken.thenCompose(optionalToken -> {
			return getUser(optionalToken.get().getUserId()).thenApply(optionalUser -> {
				updateTokenVerificationStatusAsync(optionalToken);
				updateUserVerificationStatusAsync(optionalUser);
				return optionalToken;
			});
		});
	}
	
	private void updateTokenVerificationStatusAsync(final Optional<VerificationToken> optionalToken) {
		VerificationToken token = optionalToken.get();
		token.setVerified(true);
		CompletableFuture.runAsync(() -> {
			logger.info(String.format("Persisting verified status for token %s...", token.getToken()));
			CompletableFuture<Optional<VerificationToken>> futureOptionalToken = verificationTokenRepository.updateOne(this.createUpdateVerificationStatusSpec(token.getId(), token.isVerified()));
			futureOptionalToken.thenApply(returnedOptionalToken -> {
				if(!returnedOptionalToken.isPresent()) {
					logger.info(String.format("Verification status for token %s updated.", token.getToken()));
				} else {
					logger.warn(String.format("Could not update the verification status for token %s.", token.getToken()));
				}				
				return futureOptionalToken;
			});
		});
	}
	
	private void updateUserVerificationStatusAsync(Optional<User> optionalUser) {
		final User user = optionalUser.get();
		user.setVerified(true);
		CompletableFuture.runAsync(() -> {
			logger.info(String.format("Persisting verified status for user %s...", user.getId().toString()));
			CompletableFuture<Optional<User>> futureOptionalUser = userRepostory.updateOne(this.createUpdateVerificationStatusSpec(user.getId(), user.isVerified()));
			futureOptionalUser.thenApply(returnedOptionalUser -> {
				if(!returnedOptionalUser.isPresent()) {
					logger.info(String.format("Verification status for user %s updated.", user.getId()));
				} else {
					logger.warn(String.format("Could not update the verification status for user %s.", user.getId()));
				}				
				return returnedOptionalUser;
			});
		});
	}
	
	private Specification createUpdateVerificationStatusSpec(ObjectId id, Boolean newValue) {
		return new UpdateVerificationStatusSpec(id, newValue);
	}

	private CompletableFuture<Optional<VerificationToken>> getToken(final String token) {
		logger.info(String.format("Getting token information: %s ...", token));
		final CompletableFuture<Optional<VerificationToken>> futureToken = verificationTokenRepository.findOne(new FindVerificationTokenSpec(token));
		return futureToken.thenCompose(optionalToken -> {
			if(!optionalToken.isPresent()) {
				logger.error(String.format("Could not find token: %s.", token));
				return new FailedCompletableFutureBuilder<Optional<VerificationToken>>().build(new TokenNotFoundException(token));
			} else {
				final VerificationToken verificationToken = optionalToken.get();
				if(verificationToken.hasExpired()) {
					logger.error(String.format("Token has expired: %s.", token));
					return new FailedCompletableFutureBuilder<Optional<VerificationToken>>().build(new TokenHasExpiredException(verificationToken));
				}
				else if(verificationToken.isVerified()) {
					logger.error(String.format("Token is already verified: %s.", token));
					return new FailedCompletableFutureBuilder<Optional<VerificationToken>>().build(new TokenAlreadyVerifiedException(verificationToken));
				}
				return futureToken;
			}
		});
	}
	
	private CompletableFuture<Optional<User>> getUser(final String userId) {
		logger.info(String.format("Getting user with id %s...", userId));
		final CompletableFuture<Optional<User>> futureOptionalUser = userRepostory.findOne(new FindByIdSpec(userId));
		return futureOptionalUser.thenCompose(optionalUser -> {
			if(!optionalUser.isPresent()) {
				logger.error(String.format("Could not find user for id: %s.", userId));
				return new FailedCompletableFutureBuilder<Optional<User>>().build(new UserNotFoundException("Could not find with id: " + userId));
			} else {
				if(optionalUser.get().isVerified()) {
					logger.error(String.format("User with id \"%s\" is already verified.", userId));
					return new FailedCompletableFutureBuilder<Optional<User>>().build(new UserAlreadyVerifiedException(optionalUser.get()));
				}
				return futureOptionalUser;
			}
		});
	}
		
	private CompletableFuture<Optional<VerificationToken>> generateVerificationToken(User user) {
		final String userId = user.getId().toString();
		logger.info(String.format("Generating token for user %s", userId));
		final CompletableFuture<Optional<VerificationToken>> futureActiveToken = fetchActiveVerificationToken(userId, VerificationTokenType.EMAIL_VERIFICATION);
		return futureActiveToken.thenCompose(activeVerificationToken -> {
			CompletableFuture<Optional<VerificationToken>> future = new CompletableFuture<>();
			VerificationToken token = null;
			if(activeVerificationToken.isPresent()) {
				//Use the active token instead
				token = activeVerificationToken.get();
			} else {
				//Generate a token for the user and persist it
				token = new VerificationToken(userId, VerificationTokenType.EMAIL_VERIFICATION);
				try {
					verificationTokenRepository.save(token);
					future.complete(Optional.of(token));
					logger.info(String.format("Token %s for user ID %s was generated!", token.getToken(), userId));
				} catch(Throwable ex) {
					logger.error(String.format("Failed to save token %s for user %s", token.getToken(), userId));
					return new FailedCompletableFutureBuilder<Optional<VerificationToken>>()
							.build(new ApplicationRuntimeException(
									"Failed to save the verification token for user with the given: "
											+ ex.getMessage()));
				}			
			}
			return future;
		});
	}
	
	private void sendTokenVerificationEmailAsync(User user, Optional<VerificationToken> optionalToken) {
		CompletableFuture.runAsync(() -> {
			try {
				logger.info(String.format("Sending token verification email to %s ...", user.getEmailAddress()));
				final VerificationToken verificationToken = optionalToken.get();
				final Map<String, String> templateValues = buildTemplateMap(user.getFirstName(), verificationToken.getToken());
				final String htmlMessage = templateMerger.mergeTemplateIntoString("VerifyMail", templateValues);
				final EmailMessage emailMessage = buildEmailMessage(user, htmlMessage); 
				//Send a mail with an embedded link that includes the verification token to the user
				emailService.send(emailMessage);
				logger.info(String.format("Token verification email sent for email address: %s.", user.getEmailAddress()));
			} catch (MergeException ex) {
				String errorMessage = String.format("Failed to construct email contents for %s: %s", user.getEmailAddress(), ex.getMessage()); 
				logger.error(errorMessage);
				throw new ApplicationRuntimeException(errorMessage);
			}	
		});
	}
	
	private Map<String, String> buildTemplateMap(String firstName, String token) {
		final Map<String, String> templateMap = new HashMap<>();
		templateMap.put("firstName", firstName);
		templateMap.put("verificationUrl", "www.taxibook.co.za/verify/" + token);
		templateMap.put("daysBeforeExpiry", Integer.toString(5));
		templateMap.put("generateEmailTokenUrl", "www.taxibook.co.za/verify/tokens/" + token);
		templateMap.put("helpEmailAddress", "support@rightit.co.za");
		return templateMap;
	}

	private EmailMessage buildEmailMessage(User user, String htmlMessage) {
		return new EmailMessageBuilder()
				.withSenderName("Taxibook")
				.withSenderEmail("no-reply@rightit.co.za")
				.withSubject("Your Taxibook account - Verify your E-mail address")
				.withRecipient(user.getEmailAddress())
				.withMessage(htmlMessage)
				.withContentType(EmailContentType.HTML)
				.build();
	}

	private CompletableFuture<User> fetchUserByEmailAddress(String emailAddress) {
		logger.info(String.format("Getting user with email address: %s...",emailAddress));
		final CompletableFuture<Optional<User>> futureOptionalUser = userRepostory.findOne(new FindByEmailAddressSpec(emailAddress));
		return futureOptionalUser.thenCompose(optionalUser -> {
			CompletableFuture<User> future = new CompletableFuture<>();
			if (!optionalUser.isPresent()) {
				logger.error(String.format("Failed to find user by email address: %s.", emailAddress));
				return new FailedCompletableFutureBuilder<User>().build(new UserNotFoundException("Could not find user by email address: " + emailAddress));
			} else {
				final User user = optionalUser.get();
				if(user.isVerified()) {
					logger.error(String.format("User for with email address %s is already verified.", emailAddress));
					return new FailedCompletableFutureBuilder<User>().build(new UserAlreadyVerifiedException(user));
				} else {
					future.complete(user);
				}
			}			
			return future; 
		});
	}

	private CompletableFuture<Optional<VerificationToken>> fetchActiveVerificationToken(String userId, VerificationTokenType tokenType) {
		logger.info(String.format("Check if there's %s token that is not expired yet for user %s...", tokenType, userId));
		return findActiveToken(new FindActiveVerificationTokenSpec(userId, tokenType));
	}
	
	private CompletableFuture<Optional<VerificationToken>> findActiveToken(Specification spec) {
		//Check if there's an existing token that is not expired yet
		final CompletableFuture<List<VerificationToken>> futureTokens = verificationTokenRepository.findSome(spec);
		return futureTokens.thenApply(tokens -> {
			Optional<VerificationToken> optionalToken = Optional.empty();
			for(VerificationToken token : tokens) {
				if(!token.hasExpired()) {
					logger.info(String.format("%s token for %s is found.",token.getToken(), token.getUserId()));
					optionalToken = Optional.of(token);
					break;
				}
			}
			if(!optionalToken.isPresent()) {
				logger.info(String.format("%s token for %s was not found.",optionalToken.get().getToken(), optionalToken.get().getUserId()));
			}
			return optionalToken;
		});
	}

}
