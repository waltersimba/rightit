package co.za.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

public class UserNotFoundException extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String applicationMessage) {
		super(Response.Status.NOT_FOUND.getStatusCode(), 
				Response.Status.NOT_FOUND.getStatusCode() + "01", 
				"User not found", applicationMessage);
	}

}
