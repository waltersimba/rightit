package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

import com.rightit.taxibook.domain.User;

public class UserAlreadyVerifiedException extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;

	public UserAlreadyVerifiedException(User user) {
		super(Response.Status.CONFLICT.getStatusCode(), Response.Status.CONFLICT.getStatusCode() + "02", 
				"User already verified", String.format("User is already verified: %s", user.getId()));
	}

}
