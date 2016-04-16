package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

public class DuplicateEmailAddressException extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;

	public DuplicateEmailAddressException() {
		super(Response.Status.CONFLICT.getStatusCode(), 
				Response.Status.CONFLICT.getStatusCode() + "01", 
				"User with email address already exists",
				"An attempt to create a user with duplicate email address");
	}

}
