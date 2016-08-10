package co.za.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

import co.za.rightit.commons.exceptions.CustomWebApplicationException;
import co.za.rightit.taxibook.domain.VerificationToken;

public class TokenAlreadyVerifiedException extends CustomWebApplicationException {
	
	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSASE = "The token has already been verified: %s";

	public TokenAlreadyVerifiedException(VerificationToken verificationToken) {
		super(Response.Status.CONFLICT.getStatusCode(),Response.Status.CONFLICT.getStatusCode() + "03", 
				"Token already verified", String.format(ERROR_MESSASE, verificationToken.getToken()));
	}
}
