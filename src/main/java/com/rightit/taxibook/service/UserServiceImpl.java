package com.rightit.taxibook.service;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Validator;

import org.jboss.logging.Logger;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.User.Role;
import com.rightit.taxibook.domain.User.UserBuilder;
import com.rightit.taxibook.repository.UserRepository;
import com.rightit.taxibook.repository.spec.FindByEmailAddressSpecification;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;
import com.rightit.taxibook.validation.exception.DuplicateEmailAddressException;

public class UserServiceImpl extends AbstractService implements UserService {
	
	private Logger logger = Logger.getLogger(UserServiceImpl.class);
	private UserRepository userRepository;
	@Inject
	private PasswordHashService passwordHashService;

	@Inject
	public UserServiceImpl(UserRepository userRepository, Provider<Validator> validatorProvider) {
		super(validatorProvider.get());
		this.userRepository = userRepository;
	};

	@Override
	public void createNewUser(CreateUserRequest request) {

		validate(request);

		if (hasUserWithSameEmail(request.getEmailAddress())) {
			throw new DuplicateEmailAddressException();
		} else {
			final User newUser = new UserBuilder().withFirstName(request.getFirstName())
					.withLastName(request.getLastName()).withRole(Role.fromString(request.getRole()))
					.withHashedPassword(passwordHashService.hashPassword(request.getPassword()))
					.withEmailAddress(request.getEmailAddress()).build();
			try {
				userRepository.save(newUser);
			} catch (Exception ex) {
				logger.error(ex);
				throw new ApplicationRuntimeException("Failed to persist new user: " + ex.getMessage());
			}
		}
	}

	private boolean hasUserWithSameEmail(String emailAddress) {
		User existingUser = null;
		try {
			existingUser = userRepository.findOne(new FindByEmailAddressSpecification(emailAddress));
		} catch(Exception ex) {
			logger.error(ex);
			throw new ApplicationRuntimeException("Failed to get user by email address: " + ex.getMessage());
		}	
		return existingUser != null;
	}

}
