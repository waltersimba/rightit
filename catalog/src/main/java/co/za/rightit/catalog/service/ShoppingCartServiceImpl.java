package co.za.rightit.catalog.service;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import co.za.rightit.catalog.domain.Amount;
import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.domain.ShoppingCart;
import co.za.rightit.catalog.domain.ShoppingCart.ShoppingCartItem;
import co.za.rightit.catalog.domain.ShoppingCart.ShoppingCartItemSummary;
import co.za.rightit.catalog.repository.ShoppingCartRepository;

public class ShoppingCartServiceImpl implements ShoppingCartService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);
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
	public Amount calculateTotalPrice(Collection<ShoppingCartItem> items) {
		List<Money> monies = new ArrayList<>();
		for (ShoppingCartItem item : items) {
			Product product = Preconditions.checkNotNull(item.getProduct(), "Product cannot be null");
			Amount amount = Preconditions.checkNotNull(product.getAmount(), String.format("Amount for product %s cannot be null.", product.getId()));
			Money money = Money.of(CurrencyUnit.of(amount.getCurrency()), amount.getTotal(), RoundingMode.HALF_EVEN);
			monies.add(money.multipliedBy(item.getQuantity()));
		}
		Money total = Money.total(monies);
		return new Amount(total.getCurrencyUnit(), total.getAmount());
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
