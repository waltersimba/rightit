package co.za.rightit.catalog.domain;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShoppingCart {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCart.class);
	private Map<Product, ShoppingCartItem> items = new TreeMap<>();
	
	
	public Collection<ShoppingCartItem> getItems() {
		return items.values();
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
		private final Amount amount;
		
		public ShoppingCartItemSummary(Collection<ShoppingCartItem> items, Amount amount) {
			this.items = items;
			this.amount = amount;
		}

		public Collection<ShoppingCartItem> getItems() {
			return items;
		}

		public Amount getAmount() {
			return amount;
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
