package co.za.rightit.catalog.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class ShoppingCart {

	private Map<Product, ShoppingCartItem> items = new TreeMap<>();
	
	public Collection<ShoppingCartItem> getItems() {
		return items.values();
	}
	
	public BigDecimal getTotalPrice() {
		BigDecimal totalPrice = BigDecimal.ZERO;
		for(ShoppingCartItem item : getItems()) {
			totalPrice = totalPrice.add(item.getTotalPrice());
		}
		return totalPrice;
	}
	
	public void addItem(Product product, int quantity) {
		ShoppingCartItem item = items.get(product);
		if(item == null) {
			items.put(product, new ShoppingCartItem(product, quantity));
		}
		else {
			item.incrementQuantity(quantity);
		}
	}
	
	public static class ShoppingCartItem implements Comparable<ShoppingCartItem> {

		private final Product product;
		private int quantity;
		
		public ShoppingCartItem(Product product, int quantity) {
			this.product = product;
			this.quantity = quantity;
		}
		
		public void incrementQuantity(int quantity) {
			this.quantity += quantity;			
		}

		public BigDecimal getTotalPrice() {
			return BigDecimal.valueOf(quantity).multiply(product.getPrice());
		}
		
		@Override
		public int compareTo(ShoppingCartItem o) {
			return this.product.compareTo(o.product);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((product == null) ? 0 : product.hashCode());
			result = prime * result + quantity;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof ShoppingCartItem)) return false;
			ShoppingCartItem that = (ShoppingCartItem)obj;
			if(!product.equals(that))return false; 
			return true;
		}		
		
	}
}
