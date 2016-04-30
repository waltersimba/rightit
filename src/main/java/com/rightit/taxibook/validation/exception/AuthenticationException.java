package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

public class AuthenticationException extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;

	public AuthenticationException() {
        super(Response.Status.UNAUTHORIZED.getStatusCode(), 
        		Response.Status.UNAUTHORIZED.getStatusCode() + "01", 
        		"Authentication Error", 
        		"Authentication credentials were incorrect");
    }
    
}
