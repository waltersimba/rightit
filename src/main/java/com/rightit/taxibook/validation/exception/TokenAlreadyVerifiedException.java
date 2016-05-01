package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

import com.rightit.taxibook.domain.VerificationToken;

public class TokenAlreadyVerifiedException extends BaseWebApplicationException {
	
	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSASE = "The token has already been verified: %s";

	public TokenAlreadyVerifiedException(VerificationToken verificationToken) {
		super(Response.Status.CONFLICT.getStatusCode(),Response.Status.CONFLICT.getStatusCode() + "03", 
				"Token already verified", String.format(ERROR_MESSASE, verificationToken.getToken()));
	}
}
