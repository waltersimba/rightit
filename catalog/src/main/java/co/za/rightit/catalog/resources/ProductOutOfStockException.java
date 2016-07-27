package co.za.rightit.catalog.resources;

public class ProductOutOfStockException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ProductOutOfStockException(String productTitle) {
		super("Product is out of stock: " + productTitle);
	}
}