package com.rightit.taxibook.service.user;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Validator;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.User.Role;
import com.rightit.taxibook.domain.User.UserBuilder;
import com.rightit.taxibook.repository.Repository;
import com.rightit.taxibook.repository.spec.FindByEmailAddressSpec;
import com.rightit.taxibook.service.AbstractService;
import com.rightit.taxibook.service.password.PasswordHashService;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;
import com.rightit.taxibook.validation.exception.DuplicateEmailAddressException;

public class UserServiceImpl extends AbstractService implements UserService {
	
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
				CompletableFuture<Optional<User>> failedFuture = new CompletableFuture<>();
				failedFuture.completeExceptionally(new DuplicateEmailAddressException());
				return failedFuture;
			} 
			return createNewUser(request);
		});
		return futureCreatedUser;
	}

	private CompletableFuture<Optional<User>> createNewUser(CreateUserRequest request) {
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
			futureUser.complete(Optional.of(newUser));
		} catch(Throwable ex) {
			CompletableFuture<Optional<User>> failedFuture = new CompletableFuture<>();
			failedFuture.completeExceptionally(new ApplicationRuntimeException("Failed to save new user: " + ex.getMessage()));
			return failedFuture;
		}
		return futureUser;
	}

	private CompletableFuture<Boolean> hasUserWithSameEmail(String emailAddress) {
		CompletableFuture<Boolean> futureBoolean = new CompletableFuture<>();
		try {
			CompletableFuture<Optional<User>> futureUser = repository.findOne(new FindByEmailAddressSpec(emailAddress));
			Optional<User> optionalUser = futureUser.get();
			futureBoolean.complete(optionalUser.isPresent());
		} catch(Throwable ex) {
			CompletableFuture<Boolean> failedFuture = new CompletableFuture<>();
			failedFuture.completeExceptionally(new ApplicationRuntimeException("Failed to get user by email address: " + ex.getMessage()));
			return failedFuture;
		}	
		return futureBoolean;
	}

}
