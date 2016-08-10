package co.za.rightit.catalog.resources;

import javax.ws.rs.core.Response;

import co.za.rightit.commons.exceptions.CustomWebApplicationException;

public class ProductNotFoundException extends CustomWebApplicationException {

	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String applicationMessage) {
		super(Response.Status.NOT_FOUND.getStatusCode(), 
				Response.Status.NOT_FOUND.getStatusCode() + "02", 
				"Product not found", applicationMessage);
	}

}

