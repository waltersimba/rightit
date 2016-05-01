package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

import com.rightit.taxibook.domain.User;

public class UserAlreadyVerifiedException extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSAGE = "User with email address %s is already verified.";

	public UserAlreadyVerifiedException(User user) {
		this(String.format(ERROR_MESSAGE, user.getEmailAddress()));
	}

	public UserAlreadyVerifiedException(String applicationMessage) {
		super(Response.Status.CONFLICT.getStatusCode(), Response.Status.CONFLICT.getStatusCode() + "02", 
				"User already verified", applicationMessage);
	}

}
