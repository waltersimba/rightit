package com.rightit.taxibook.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Validator;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.domain.VerificationToken.VerificationTokenType;
import com.rightit.taxibook.repository.UseRepository;
import com.rightit.taxibook.repository.VerificationTokenRepository;
import com.rightit.taxibook.repository.spec.FindActiveVerificationTokenSpecification;
import com.rightit.taxibook.repository.spec.FindByEmailAddressSpecification;
import com.rightit.taxibook.repository.spec.Specification;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;
import com.rightit.taxibook.validation.exception.UserAlreadyVerified;
import com.rightit.taxibook.validation.exception.UserNotFoundException;

public class VerificationTokenServiceImpl extends AbstractService implements VerificationTokenService {

	private UseRepository userRepostory = null;
	private VerificationTokenRepository verificationTokenRepository = null;

	@Inject
	public VerificationTokenServiceImpl(UseRepository userRepository,
			VerificationTokenRepository verificationTokenRepository, Provider<Validator> validatorProvider) {
		super(validatorProvider.get());
		this.userRepostory = userRepository;
		this.verificationTokenRepository = verificationTokenRepository;
	}

	@Override
	public Optional<VerificationToken> generateEmailVerificationToken(EmailVerificationRequest request) {
		
		validate(request);
		
		final Optional<User> optionalUser = fetchUserByEmailAddress(request.getEmailAddress());
		
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("Failed to find user with a given email address");
		}
		
		if(optionalUser.get().isVerified()) {
			throw new UserAlreadyVerified();
		}
		
		final String userId = optionalUser.get().getId().toString();
		VerificationToken token = null;
		final Optional<VerificationToken> activeVerificationToken = fetchActiveVerificationToken(userId, VerificationTokenType.EMAIL_VERIFICATION);
		if(activeVerificationToken.isPresent()) {
			token = activeVerificationToken.get();
		} else {
			//Generate a token for the user and persist it
			token = new VerificationToken(userId, VerificationTokenType.EMAIL_VERIFICATION);
			try {
				verificationTokenRepository.save(token);
			} catch(Exception ex) {
				throw new ApplicationRuntimeException("Failed to save the verification token for user with the given: " + ex.getMessage());
			}			
		}
		//TODO: Send a mail with an embedded link that includes the verification token to the user
		return Optional.of(token);
	}

	@Override
	public Optional<VerificationToken> verify(String token) {
		return Optional.empty();
	}

	private Optional<User> fetchUserByEmailAddress(String emailAddress) {
		return userRepostory.findOne(new FindByEmailAddressSpecification(emailAddress));
	}
	
	private Optional<VerificationToken> fetchActiveVerificationToken(String userId, VerificationTokenType tokenType) {
		Optional<VerificationToken> optionalToken = Optional.empty();
		try {
			final Specification findActiveTokenSpec = new FindActiveVerificationTokenSpecification(userId, tokenType);
			for(VerificationToken token : verificationTokenRepository.findSome(findActiveTokenSpec)) {
				if(!token.hasExpired()) {
					optionalToken = Optional.of(token);
					break;
				}
			}
		} catch(Exception ex) {
			throw new ApplicationRuntimeException("Failed to find active verification token for user: " + ex.getMessage());
		}
		return optionalToken;
	}

}
