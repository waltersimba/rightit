package co.za.rightit.catalog.service;

import java.math.BigDecimal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import co.za.rightit.catalog.domain.ShoppingCart;
import co.za.rightit.catalog.domain.ShoppingCart.ShoppingCartItem;
import co.za.rightit.catalog.domain.ShoppingCart.ShoppingCartItemSummary;
import co.za.rightit.catalog.repository.ShoppingCartRepository;

public class ShoppingCartServiceImpl implements ShoppingCartService {

	private final HttpServletRequest request;
	private final ShoppingCartRepository repository;
	
	@Inject
	public ShoppingCartServiceImpl(ShoppingCartRepository repository, HttpServletRequest request) {
		this.repository = repository;
		this.request = request;
	}
	
	@Override
	public ShoppingCartItemSummary getSummary(Collection<ShoppingCartItem> items) {
		return new ShoppingCartItemSummary(items, calculateTotalPrice(items));
	}
	
	@Override
	public BigDecimal calculateTotalPrice(Collection<ShoppingCartItem> items) {
		BigDecimal totalPrice = BigDecimal.ZERO;
		for(ShoppingCartItem item : items) {
			totalPrice = totalPrice.add(item.getTotalPrice());
		}
		return totalPrice;
	}

	@Override
	public ShoppingCart getShoppingCart() {
		return repository.getCurrentShoppingCart(request);
	}

	@Override
	public ShoppingCart clearShoppingCart() {
		repository.resetCurrentShoppingCart(request);
		return repository.getCurrentShoppingCart(request);
	} 

}
