package com.rightit.taxibook.resource;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.service.user.CreateUserRequest;
import com.rightit.taxibook.service.user.UserService;

@Path("users")
public class UserResource {

	private UserService userService;
	
	@Inject
	public UserResource(UserService userService) {
		this.userService = userService;
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
    public Response signupUser(CreateUserRequest request) throws Throwable {
		CompletableFuture<Optional<User>> futureUser = userService.createUser(request);
		Optional<User> optionalUser = null;
		try {
			optionalUser = futureUser.get();
		} catch (InterruptedException ex) {
			throw ex;
		} catch (ExecutionException ex) {
			throw ex.getCause();
		}
        return Response.ok(optionalUser.get()).build();
    }
}
