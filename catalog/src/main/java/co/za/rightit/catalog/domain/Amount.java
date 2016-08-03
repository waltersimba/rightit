package co.za.rightit.catalog.domain;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;

public class Amount {

	private String currency;
	private String symbol;
	private BigDecimal total;
	
	public Amount(BigDecimal total) {
		this(null, total);
	} 
	
	public Amount(CurrencyUnit currencyUnit, BigDecimal total) {
		this.currency = currencyUnit.getCurrencyCode();
		this.symbol = currencyUnit.getSymbol();
		this.total = total;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
