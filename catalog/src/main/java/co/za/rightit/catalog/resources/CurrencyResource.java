package co.za.rightit.catalog.resources;

import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import co.za.rightit.catalog.domain.CurrencyInfo;

@Path("currencies")
public class CurrencyResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response currencies() {
		return Response.ok(getAllCurrencies())
				.header("cache-control", "public, max-age=" + TimeUnit.SECONDS.convert(365, TimeUnit.DAYS)).build();
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
