package co.za.rightit.catalog.repository;

import javax.servlet.http.HttpServletRequest;

import co.za.rightit.catalog.domain.ShoppingCart;

public interface ShoppingCartRepository {

	ShoppingCart getCurrentShoppingCart(HttpServletRequest request);
	
	void resetCurrentShoppingCart(HttpServletRequest request);
}
