package co.za.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

import co.za.rightit.commons.exceptions.CustomWebApplicationException;

public class TokenNotFoundException extends CustomWebApplicationException {

	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSAGE = "Token could not be found: %s";
	
	public TokenNotFoundException(String token) {
        super(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.getStatusCode() + "02", 
        		"Token Not Found", String.format(ERROR_MESSAGE, token));
    }
}
