package co.za.rightit.catalog.resources;

import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import co.za.rightit.catalog.domain.CurrencyInfo;

@Path("currency")
public class CurrencyResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response currencies() {
		return Response.ok(getAllCurrencies()).build();
	}

	public static Map<String, CurrencyInfo> getAllCurrencies() {
		Map<String, CurrencyInfo> currencies = new TreeMap<String, CurrencyInfo>();
		for (Locale locale : Locale.getAvailableLocales()) {
			if (StringUtils.isNotBlank(locale.getCountry())) {
				Currency currency = Currency.getInstance(locale);
				currencies.put(currency.getCurrencyCode(), new CurrencyInfo()
						.withCode(currency.getCurrencyCode())
						.withSymbol(currency.getSymbol(locale))
						.withLanguage(locale.toLanguageTag())
						.withCountry(locale.getCountry())
						.withDisplayName(currency.getDisplayName(locale)));
			}
		}
		return currencies;
	}
}
