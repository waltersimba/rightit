package com.rightit.taxibook.validation.exception;

import javax.ws.rs.core.Response;

public class ApplicationRuntimeException extends BaseWebApplicationException {

	private static final long serialVersionUID = 1L;

	public ApplicationRuntimeException(String applicationMessage) {
        super(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 
        		Response.Status.INTERNAL_SERVER_ERROR.getStatusCode() + "01", 
        		"Internal System error", applicationMessage);
    }
}
