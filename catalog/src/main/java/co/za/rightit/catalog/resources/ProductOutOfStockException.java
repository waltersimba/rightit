package co.za.rightit.catalog.resources;

import javax.ws.rs.core.Response;

import co.za.rightit.commons.exceptions.CustomWebApplicationException;

public class ProductOutOfStockException extends CustomWebApplicationException {

	private static final long serialVersionUID = 1L;

	public ProductOutOfStockException(String applicationMessage) {
		super(Response.Status.BAD_REQUEST.getStatusCode(), 
				Response.Status.BAD_REQUEST.getStatusCode() + "02", 
				"Product is out of stock", applicationMessage);
	}

}