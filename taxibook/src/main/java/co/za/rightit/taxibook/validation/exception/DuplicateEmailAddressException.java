package co.za.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

import co.za.rightit.commons.exceptions.CustomWebApplicationException;

public class DuplicateEmailAddressException extends CustomWebApplicationException {

	private static final long serialVersionUID = 1L;

	public DuplicateEmailAddressException() {
		this("An attempt to create a user with duplicate email address");
	}
	
	public DuplicateEmailAddressException(String applicationMessage) {
		super(Response.Status.CONFLICT.getStatusCode(), 
				Response.Status.CONFLICT.getStatusCode() + "01", 
				"User with email address already exists",applicationMessage);
	}

}
