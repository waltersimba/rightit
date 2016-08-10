package co.za.rightit.catalog.domain;

public class CurrencyInfo {

	private String code;
	private String symbol;
	private String language;
	private String country;
	private String displayName;
		
	public String getCode() {
		return code;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getLanguage() {
		return language;
	}
	
	public String getCountry() {
		return country;
	}

	public String getDisplayName() {
		return displayName;
	}

	public CurrencyInfo withCode(String code) {
		this.code = code;
		return this;
	}
	
	public CurrencyInfo withSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}
	
	public CurrencyInfo withLanguage(String language) {
		this.language = language;
		return this;
	}
	
	public CurrencyInfo withCountry(String country) {
		this.country = country;
		return this;
	}
	
	public CurrencyInfo withDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	@Override
	public String toString() {
		return "CurrencyInfo [code=" + code + ", symbol=" + symbol + ", language=" + language + ", country=" + country
				+ ", displayName=" + displayName + "]";
	}

}
