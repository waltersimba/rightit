package com.rightit.taxibook.service.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Validator;

import org.apache.log4j.Logger;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.domain.VerificationToken.VerificationTokenType;
import com.rightit.taxibook.repository.UseRepository;
import com.rightit.taxibook.repository.VerificationTokenRepository;
import com.rightit.taxibook.repository.spec.FindActiveVerificationTokenSpecification;
import com.rightit.taxibook.repository.spec.FindByEmailAddressSpecification;
import com.rightit.taxibook.repository.spec.Specification;
import com.rightit.taxibook.service.AbstractService;
import com.rightit.taxibook.service.mail.EmailMessage;
import com.rightit.taxibook.service.mail.EmailMessage.EmailContentType;
import com.rightit.taxibook.service.mail.EmailMessage.EmailMessageBuilder;
import com.rightit.taxibook.service.mail.EmailService;
import com.rightit.taxibook.template.MergeException;
import com.rightit.taxibook.template.TemplateMerger;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;
import com.rightit.taxibook.validation.exception.UserAlreadyVerified;
import com.rightit.taxibook.validation.exception.UserNotFoundException;

public class VerificationTokenServiceImpl extends AbstractService implements VerificationTokenService {

	private Logger logger = Logger.getLogger(VerificationTokenServiceImpl.class);
	@Inject private EmailService emailService;
	@Inject private TemplateMerger templateMerger;
	@Inject private UseRepository userRepostory;
	@Inject private VerificationTokenRepository verificationTokenRepository;

	@Inject
	public VerificationTokenServiceImpl(Provider<Validator> validatorProvider) {
		super(validatorProvider.get());
	}

	@Override
	public CompletableFuture<Optional<VerificationToken>> generateEmailVerificationToken(EmailVerificationRequest request) {
		
		validate(request);
		
		CompletableFuture<User> futureUser = fetchUserByEmailAddress(request.getEmailAddress());
		CompletableFuture<Optional<VerificationToken>> futureToken = futureUser.thenCompose(user -> {
			return generateVerificationToken(user).thenCompose(optionalToken -> {
				return sendTokenVerificationEmail(user, optionalToken);
			});
		});
		
		return futureToken;
	}

	@Override
	public Optional<VerificationToken> verify(String token) {
		return Optional.empty();
	}
		
	private CompletableFuture<Optional<VerificationToken>> generateVerificationToken(User user) {
		final String userId = user.getId().toString();
		CompletableFuture<Optional<VerificationToken>> futureActiveToken = fetchActiveVerificationToken(userId, VerificationTokenType.EMAIL_VERIFICATION);
		CompletableFuture<Optional<VerificationToken>> futureToken = futureActiveToken.thenApply(activeVerificationToken -> {
			VerificationToken token = null;
			if(activeVerificationToken.isPresent()) {
				//Use the active token instead
				token = activeVerificationToken.get();
			} else {
				//Generate a token for the user and persist it
				token = new VerificationToken(userId, VerificationTokenType.EMAIL_VERIFICATION);
				try {
					verificationTokenRepository.save(token);
					logger.info(String.format("Token %s for user ID %s generated!", token.getToken(), userId));
				} catch(Throwable ex) {
					futureActiveToken.completeExceptionally(new ApplicationRuntimeException("Failed to save the verification token for user with the given: " + ex.getMessage()));
				}			
			}
			return Optional.of(token);
		});
		return futureToken;
	}
	
	private CompletableFuture<Optional<VerificationToken>> sendTokenVerificationEmail(User user, Optional<VerificationToken> optionalToken) {
		CompletableFuture<Optional<VerificationToken>> tokenFuture = new CompletableFuture<>();
		try {
			final VerificationToken verificationToken = optionalToken.get();
			final Map<String, String> templateValues = buildTemplateMap(user.getFirstName(), verificationToken.getToken());
			final String htmlMessage = templateMerger.mergeTemplateIntoString("VerifyMail", templateValues);
			final EmailMessage emailMessage = buildEmailMessage(user, htmlMessage); 
			//Send a mail with an embedded link that includes the verification token to the user
			emailService.send(emailMessage);
			tokenFuture.complete(optionalToken);
		} catch (MergeException ex) {
			CompletableFuture<Optional<VerificationToken>> failedFuture = new CompletableFuture<>();
			failedFuture.completeExceptionally(new ApplicationRuntimeException("Failed to send token verification email: " + ex.getMessage()));
			return failedFuture;
		}	
		return tokenFuture;
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
		CompletableFuture<Optional<User>> futureOptionalUser = userRepostory.findOne(new FindByEmailAddressSpecification(emailAddress));
		CompletableFuture<User> futureUser = futureOptionalUser.thenApply(optionalUser -> {
			if (!optionalUser.isPresent()) {
				futureOptionalUser.completeExceptionally(new UserNotFoundException("Failed to find user with a given email address"));
			} 
			final User user = optionalUser.get();
			if(user.isVerified()) {
				futureOptionalUser.completeExceptionally(new UserAlreadyVerified());
			}
			return user;
		});
		return futureUser;
	}
	
	private CompletableFuture<Optional<VerificationToken>> fetchActiveVerificationToken(String userId, VerificationTokenType tokenType) {
		//Check if there's an existing token that is not expired yet
		final Specification findActiveTokenSpec = new FindActiveVerificationTokenSpecification(userId, tokenType);
		final CompletableFuture<List<VerificationToken>> futureTokens = verificationTokenRepository.findSome(findActiveTokenSpec);
		final CompletableFuture<Optional<VerificationToken>> futureOptionalToken = futureTokens.thenApply(tokens -> {
			Optional<VerificationToken> optionalToken = Optional.empty();
			for(VerificationToken token : tokens) {
				if(!token.hasExpired()) {
					optionalToken = Optional.of(token);
					break;
				}
			}
			return optionalToken;
		});
		return futureOptionalToken;
	}

}
