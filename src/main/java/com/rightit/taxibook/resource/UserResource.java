package com.rightit.taxibook.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.rightit.taxibook.service.CreateUserRequest;
import com.rightit.taxibook.service.UserService;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserResource {

	private UserService userService;
	private Logger logger = Logger.getLogger(UserResource.class);
	
	@Inject
	public UserResource(UserService userService) {
		this.userService = userService;
	}
	
	@POST
    public Response signupUser(CreateUserRequest request) {
		logger.info("Request: " + request.getEmailAddress());
		userService.createNewUser(request);
        return Response.ok().build();
    }
}
