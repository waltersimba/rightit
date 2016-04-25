package com.rightit.taxibook.service.user;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.rightit.taxibook.domain.User;

public interface UserService {

	CompletableFuture<Optional<User>> createUser(CreateUserRequest request);
	
}
