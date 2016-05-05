package com.rightit.taxibook.service.user;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.User.Role;
import com.rightit.taxibook.domain.User.UserBuilder;
import com.rightit.taxibook.repository.Repository;
import com.rightit.taxibook.service.AbstractService;
import com.rightit.taxibook.service.password.PasswordHashService;
import com.rightit.taxibook.spec.query.FindByEmailAddressSpec;
import com.rightit.taxibook.util.FailedCompletableFutureBuilder;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;
import com.rightit.taxibook.validation.exception.DuplicateEmailAddressException;

public class UserServiceImpl extends AbstractService implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private Repository<User> repository;
	@Inject
	private PasswordHashService passwordHashService;

	@Inject
	public UserServiceImpl(Repository<User> repository, Provider<Validator> validatorProvider) {
		super(validatorProvider.get());
		this.repository = repository;
	};

	@Override
	public CompletableFuture<Optional<User>> createUser(CreateUserRequest request) {

		validate(request);
				
		CompletableFuture<Boolean> futureHasUserWithSameEmail = hasUserWithSameEmail(request.getEmailAddress());
		CompletableFuture<Optional<User>> futureCreatedUser = futureHasUserWithSameEmail.thenCompose(hasUserWithSameEmail -> {
			if (hasUserWithSameEmail) {
				String errorMessage = String.format("Already has user with duplicate email address: %s", request.getEmailAddress());
				logger.error(errorMessage);
				return new FailedCompletableFutureBuilder<Optional<User>>().build(new DuplicateEmailAddressException(errorMessage));
			} 
			return createNewUser(request);
		});
		return futureCreatedUser;
	}

	private CompletableFuture<Optional<User>> createNewUser(CreateUserRequest request) {
		logger.info(String.format("Creating new user with email address: %s...", request.getEmailAddress()));
		CompletableFuture<Optional<User>> futureUser = new CompletableFuture<>();
		try {
			final User newUser = new UserBuilder()
					.withFirstName(request.getFirstName())
					.withLastName(request.getLastName())
					.withRole(Role.fromString(request.getRole()))
					.withHashedPassword(passwordHashService.hashPassword(request.getPassword()))
					.withEmailAddress(request.getEmailAddress())
					.withVerified(Boolean.FALSE)
					.build();
			repository.save(newUser);
			logger.info(String.format("User with email address %s created successfully.", request.getEmailAddress()));
			futureUser.complete(Optional.of(newUser));
		} catch(Throwable ex) {
			String errorMessage = String.format("Failed to create user with email address %s: %s", request.getEmailAddress(), ex.getMessage()); 
			logger.error(errorMessage);
			return new FailedCompletableFutureBuilder<Optional<User>>().build(new ApplicationRuntimeException(errorMessage));
		}
		return futureUser;
	}

	private CompletableFuture<Boolean> hasUserWithSameEmail(String emailAddress) {
		logger.info(String.format("Checking if user with email address %s already exists ...", emailAddress));
		CompletableFuture<Boolean> futureBoolean = new CompletableFuture<>();
		try {
			CompletableFuture<Optional<User>> futureUser = repository.findOne(new FindByEmailAddressSpec(emailAddress));
			Optional<User> optionalUser = futureUser.get();
			futureBoolean.complete(optionalUser.isPresent());
			logger.info(String.format("User with email address %s found ? %s.", emailAddress, optionalUser.isPresent()));
		} catch(Throwable ex) {
			String errorMessage = String.format("Failed to get user with email address %s: %s", emailAddress, ex.getMessage()); 
			logger.error(errorMessage);
			return new FailedCompletableFutureBuilder<Boolean>()
					.build(new ApplicationRuntimeException(errorMessage));
		}	
		return futureBoolean;
	}

}
