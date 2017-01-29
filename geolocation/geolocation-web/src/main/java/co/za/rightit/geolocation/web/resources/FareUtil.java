package co.za.rightit.geolocation.web.resources;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import co.za.rightit.geolocation.model.FareResponse.Measurement;

public final class FareUtil {

	//Averages between UberBlack and UberX
	private static final Double BASE_FEE = 10.00;
	private static final Double PER_MINUTE = 1.00;
	private static final Double PER_KILOMETER = 10.25;
	private static final Double MINIMUM_FARE = 45.00;
	
	private FareUtil() {}
	
	public static Measurement calculateCost(Long distanceInMeters, Long durationInSeconds) {
		final Long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds);
		final Double cost = Math.max(MINIMUM_FARE, BASE_FEE + PER_MINUTE * minutes + (distanceInMeters / 1000) * PER_KILOMETER);
		final Locale locale = Locale.forLanguageTag("en-ZA");
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        formatter.setCurrency(Currency.getInstance("ZAR"));
		return new Measurement(formatter.format(cost), cost);
	}
	
}
