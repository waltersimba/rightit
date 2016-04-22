package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

public class UserAlreadyVerified extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;

	public UserAlreadyVerified() {
		super(Response.Status.CONFLICT.getStatusCode(), 
				Response.Status.CONFLICT.getStatusCode() + "02", 
				"User already verified",
				"User email already been verified");
	}

}
