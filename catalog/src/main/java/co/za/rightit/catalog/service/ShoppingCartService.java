package co.za.rightit.catalog.service;

import java.math.BigDecimal;
import java.util.Collection;

import co.za.rightit.catalog.domain.ShoppingCart;
import co.za.rightit.catalog.domain.ShoppingCart.ShoppingCartItem;
import co.za.rightit.catalog.domain.ShoppingCart.ShoppingCartItemSummary;

public interface ShoppingCartService {
	
	ShoppingCartItemSummary getSummary(Collection<ShoppingCartItem> items);
	
	BigDecimal calculateTotalPrice (Collection<ShoppingCartItem> items);
	
	ShoppingCart getShoppingCart();
	
	ShoppingCart clearShoppingCart();
}
