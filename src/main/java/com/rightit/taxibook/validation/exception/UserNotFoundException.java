package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

public class UserNotFoundException extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
		super(Response.Status.NOT_FOUND.getStatusCode(), 
				Response.Status.NOT_FOUND.getStatusCode() + "02", 
				"User not found",
				"Failed to find user with a given id");
	}
	
	public UserNotFoundException(String developerMessage) {
		super(Response.Status.NOT_FOUND.getStatusCode(), 
				Response.Status.NOT_FOUND.getStatusCode() + "02", 
				"User not found", developerMessage);
	}

}
