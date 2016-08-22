package co.za.rightit.catalog.domain;

import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import co.za.rightit.catalog.resources.ProductOutOfStockException;

public class ShoppingCart {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCart.class);
	private Map<Product, ShoppingCartItem> items = new TreeMap<>();
	
	public boolean isEmpty() {
		return getItems().isEmpty();
	}
	
	public Collection<ShoppingCartItem> getItems() {
		return items.values();
	}
		
	public void addOrUpdateItem(Product product, int quantity) {
		if(product.isOutOfStock()) {
			LOGGER.error("Failed to add/update product: Out of stock");
			throw new ProductOutOfStockException("Failed to add/update product"); 
		}
		ShoppingCartItem item = items.get(product);
		if(item == null) {
			items.put(product, new ShoppingCartItem(product, quantity));
		}
		else {
			if(quantity <= 0) {
				items.remove(product);
			} else {
				item.setQuantity(quantity);
			}
		}
	}
	
	public void clearItems() {
		items.clear();
	}
	
	public static class ShoppingCartItemSummary {
		
		private final Collection<ShoppingCartItem> items;
		private final Amount total;
		
		public ShoppingCartItemSummary(Collection<ShoppingCartItem> items, Amount total) {
			this.items = items;
			this.total = total;
		}

		public Collection<ShoppingCartItem> getItems() {
			return items;
		}

		public Amount getTotal() {
			return total;
		}
		
	}
	
	public static class ShoppingCartItem implements Comparable<ShoppingCartItem> {

		private final Product product;
		private int quantity;
		
		public ShoppingCartItem(Product product, int quantity) {
			this.product = product;
			this.quantity = quantity;
		}
		
		public void setQuantity(int quantity) {
			this.quantity = quantity;			
		}
		
		public int getQuantity() {
			return quantity;
		}

		public Product getProduct() {
			return product;
		}
		
		@JsonProperty("sub_total")
		public Amount getSubTotal() {
			Amount amount = Preconditions.checkNotNull(product.getAmount(), String.format("Amount for product %s cannot be null.", product.getId()));
			Money money = Money.of(CurrencyUnit.of(amount.getCurrency()), amount.getTotal(), RoundingMode.HALF_EVEN);
			Money total = money.multipliedBy(quantity);
			return new Amount(CurrencyUnit.of(amount.getCurrency()), total.getAmount());
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
