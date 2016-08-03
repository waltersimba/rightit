package co.za.rightit.catalog.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShoppingCart {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCart.class);
	private Map<Product, ShoppingCartItem> items = new TreeMap<>();
	
	public ShoppingCartItemSummary getSummary() {
		return new ShoppingCartItemSummary(getItems(), getTotalPrice());
	}
	
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
	
	
	public void addOrUpdateItem(Product product, int quantity) {
		ShoppingCartItem item = items.get(product);
		if(item == null) {
			items.put(product, new ShoppingCartItem(product, quantity));
		}
		else {
			item.incrementQuantity(quantity);
			if(item.quantity <= 0) {
				items.remove(product);
			}
		}
	}
	
	public void clearItems() {
		items.clear();
	}
	
	public static class ShoppingCartItemSummary {
		
		private final Collection<ShoppingCartItem> items;
		private final BigDecimal totalPrice;
		
		public ShoppingCartItemSummary(Collection<ShoppingCartItem> items, BigDecimal totalPrice) {
			this.items = items;
			this.totalPrice = totalPrice;
		}

		public Collection<ShoppingCartItem> getItems() {
			return items;
		}

		public BigDecimal getTotalPrice() {
			return totalPrice;
		}

		@Override
		public String toString() {
			return "ShoppingCartItemSummary [items=" + items + ", totalPrice=" + totalPrice + "]";
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
		
		public int getQuantity() {
			return quantity;
		}

		public Product getProduct() {
			return product;
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

		@Override
		public String toString() {
			return "ShoppingCartItem [product=" + product + ", quantity=" + quantity + "]";
		}
				
	}
}
