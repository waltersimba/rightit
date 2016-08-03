package co.za.rightit.catalog.service;

import java.util.Collection;

import co.za.rightit.catalog.domain.Amount;
import co.za.rightit.catalog.domain.ShoppingCart;
import co.za.rightit.catalog.domain.ShoppingCart.ShoppingCartItem;
import co.za.rightit.catalog.domain.ShoppingCart.ShoppingCartItemSummary;

public interface ShoppingCartService {
	
	ShoppingCartItemSummary getSummary(Collection<ShoppingCartItem> items);
	
	Amount calculateTotalAmount (Collection<ShoppingCartItem> items);
	
	ShoppingCart getShoppingCart();
	
	ShoppingCart clearShoppingCart();
}
