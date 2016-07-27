package co.za.rightit.catalog.resources;

public class ProductNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String productId) {
		super("No product found for id: " + productId);
	}
}
