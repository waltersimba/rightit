package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

import com.rightit.taxibook.domain.VerificationToken;

public class TokenHasExpiredException extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSAGE = "An attempt was made to load a token that has expired: %s";
	
	public TokenHasExpiredException(VerificationToken verificationToken) {
        super(Response.Status.FORBIDDEN.getStatusCode(), Response.Status.FORBIDDEN.getStatusCode() + "01", 
        		"Token has expired", String.format(ERROR_MESSAGE, verificationToken.getToken()));
    }

}
