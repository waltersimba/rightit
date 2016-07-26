package co.za.rightit.taxibook.service.user;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import co.za.rightit.taxibook.domain.User;

public interface UserService {

	CompletableFuture<Optional<User>> createUser(CreateUserRequest request);
	
}
