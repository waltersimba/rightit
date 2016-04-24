package com.rightit.taxibook.service.user;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Validator;

import org.jboss.logging.Logger;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.User.Role;
import com.rightit.taxibook.domain.User.UserBuilder;
import com.rightit.taxibook.repository.Repository;
import com.rightit.taxibook.repository.spec.FindByEmailAddressSpecification;
import com.rightit.taxibook.service.AbstractService;
import com.rightit.taxibook.service.password.PasswordHashService;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;
import com.rightit.taxibook.validation.exception.DuplicateEmailAddressException;

public class UserServiceImpl extends AbstractService implements UserService {
	
	private Logger logger = Logger.getLogger(UserServiceImpl.class);
	private Repository<User> repository;
	@Inject
	private PasswordHashService passwordHashService;

	@Inject
	public UserServiceImpl(Repository<User> repository, Provider<Validator> validatorProvider) {
		super(validatorProvider.get());
		this.repository = repository;
	};

	@Override
	public void createNewUser(CreateUserRequest request) {

		validate(request);

		if (hasUserWithSameEmail(request.getEmailAddress())) {
			throw new DuplicateEmailAddressException();
		} else {
			final User newUser = new UserBuilder()
					.withFirstName(request.getFirstName())
					.withLastName(request.getLastName())
					.withRole(Role.fromString(request.getRole()))
					.withHashedPassword(passwordHashService.hashPassword(request.getPassword()))
					.withEmailAddress(request.getEmailAddress())
					.withVerified(Boolean.FALSE)
					.build();
			try {
				repository.save(newUser);
			} catch (Exception ex) {
				logger.error(ex);
				throw new ApplicationRuntimeException("Failed to persist new user: " + ex.getMessage());
			}
		}
	}

	private boolean hasUserWithSameEmail(String emailAddress) {
		boolean userWithSameEmailFound = false;
		try {
			CompletableFuture<Optional<User>> futureUser = repository.findOne(new FindByEmailAddressSpecification(emailAddress));
			Optional<User> optionalUser = futureUser.get();
			userWithSameEmailFound = optionalUser.isPresent();
		} catch(Exception ex) {
			logger.error(ex);
			throw new ApplicationRuntimeException("Failed to get user by email address: " + ex.getMessage());
		}	
		return userWithSameEmailFound;
	}

}
