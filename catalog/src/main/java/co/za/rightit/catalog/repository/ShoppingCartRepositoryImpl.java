package co.za.rightit.catalog.repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import co.za.rightit.catalog.domain.ShoppingCart;

public class ShoppingCartRepositoryImpl implements ShoppingCartRepository {

	@Override
	public ShoppingCart getCurrentShoppingCart(HttpServletRequest request) {
		HttpSession session = request.getSession();
		ShoppingCart cart = (ShoppingCart) session.getAttribute(ShoppingCart.class.getName());
		if(cart == null) {
			cart = new ShoppingCart();
			session.setAttribute(ShoppingCart.class.getName(), cart);
		}
		return cart;
	}

	@Override
	public void resetCurrentShoppingCart(HttpServletRequest request) {
		request.getSession().removeAttribute(ShoppingCart.class.getName());
	}	

}
