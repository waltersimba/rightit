package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

import com.rightit.taxibook.domain.User;

public class UserNotVerifiedException extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSAGE = "User with email address %s is not verified.";

	public UserNotVerifiedException(User user) {
		this(String.format(ERROR_MESSAGE, user.getEmailAddress()));
	}

	public UserNotVerifiedException(String applicationMessage) {
		super(Response.Status.CONFLICT.getStatusCode(), Response.Status.CONFLICT.getStatusCode() + "04", 
				"User not verified", applicationMessage);
	}

}
