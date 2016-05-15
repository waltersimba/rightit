package com.rightit.taxibook.service.verify;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.domain.VerificationToken.VerificationTokenType;
import com.rightit.taxibook.repository.VerificationTokenRepository;
import com.rightit.taxibook.spec.Specification;
import com.rightit.taxibook.spec.query.FindActiveVerificationTokenSpec;
import com.rightit.taxibook.util.FailedCompletableFutureBuilder;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;

public class TokenGeneratorImpl implements TokenGenerator {

	private Logger LOGGER = LoggerFactory.getLogger(TokenGeneratorImpl.class);
	
	@Inject 
	private VerificationTokenRepository verificationTokenRepository;
	
	
	@Override
	public CompletableFuture<Optional<VerificationToken>> generateVerificationToken(User user, VerificationTokenType tokenType, DateTime expires) {
		final String userId = user.getId().toString();
		LOGGER.info(String.format("Generating token for user %s", userId));
		final CompletableFuture<Optional<VerificationToken>> futureActiveToken = fetchActiveVerificationToken(userId, tokenType);
		return futureActiveToken.thenCompose(activeVerificationToken -> {
			CompletableFuture<Optional<VerificationToken>> future = new CompletableFuture<>();
			VerificationToken token = null;
			if(activeVerificationToken.isPresent()) {
				//Use the active token instead
				token = activeVerificationToken.get();
			} else {
				//Generate a token for the user and persist it
				token = new VerificationToken(userId, tokenType, expires);
				try {
					verificationTokenRepository.save(token);
					LOGGER.info(String.format("Token %s for user ID %s was generated!", token.getToken(), userId));
				} catch(Throwable ex) {
					String errorMessage = String.format("Failed to save token %s for user %s: %s", token.getToken(), userId, ex.getMessage());
					LOGGER.error(errorMessage);
					return new FailedCompletableFutureBuilder<Optional<VerificationToken>>()
							.build(new ApplicationRuntimeException(errorMessage));
				}			
			}
			future.complete(Optional.of(token));
			return future;
		});
	}
	
	private CompletableFuture<Optional<VerificationToken>> fetchActiveVerificationToken(String userId, VerificationTokenType tokenType) {
		LOGGER.info(String.format("Check if there's %s token that is not expired yet for user %s...", tokenType, userId));
		final CompletableFuture<Optional<VerificationToken>> future = findActiveToken(
				new FindActiveVerificationTokenSpec(userId, tokenType));
		return future.thenApply(optionalToken -> {
			if(!optionalToken.isPresent()) {
				LOGGER.info("{} token for {} is found.",tokenType, userId);
			}
			return optionalToken;
		});
	}
	
	private CompletableFuture<Optional<VerificationToken>> findActiveToken(Specification spec) {
		//Check if there's an existing token that is not expired yet
		final CompletableFuture<List<VerificationToken>> futureTokens = verificationTokenRepository.findSome(spec);
		return futureTokens.thenApply(tokens -> {
			Optional<VerificationToken> optionalToken = Optional.empty();
			for(VerificationToken token : tokens) {
				if(!token.hasExpired()) {
					LOGGER.info(String.format("%s token for %s is found.",token.getToken(), token.getUserId()));
					optionalToken = Optional.of(token);
					break;
				}
			}
			return optionalToken;
		});
	}

}
