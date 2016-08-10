package co.za.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

import co.za.rightit.commons.exceptions.CustomWebApplicationException;

public class AuthenticationException extends CustomWebApplicationException {

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String applicationMessage) {
        super(Response.Status.UNAUTHORIZED.getStatusCode(), 
        		Response.Status.UNAUTHORIZED.getStatusCode() + "01", 
        		"Authentication Failed", 
        		applicationMessage);
    }
	
	public AuthenticationException() {
		this("Authentication credentials were incorrect");
    }
    
}
