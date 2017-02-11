package co.za.rightit.fare.api.options;

import java.util.Currency;
import java.util.Locale;

import com.google.maps.model.TrafficModel;

public final class FareOptionsDefault {

	private static final Double BASE_FEE = 5.00;
	private static final Double PER_MINUTE = 0.75;
	private static final Double PER_KILOMETER = 7.50;
	private static final Double MINIMUM_FARE = 25.00;
	
	public static FareOptions getFareOptions() {
		return FareOptions.newBuilder()
				.withDistanceMatrixApiKey("AIzaSyCVw-9nXwjDzYd-w86VQAvUYr_mcfoI6L8")
				.withTrafficModel(TrafficModel.PESSIMISTIC)
				.withBaseFee(BASE_FEE)
				.withPerMinute(PER_MINUTE)
				.withPerKilometer(PER_KILOMETER)
				.withMinimunFare(MINIMUM_FARE)
				.withLocale(Locale.forLanguageTag("en-ZA"))
				.withCurrency(Currency.getInstance("ZAR"))
				.build();
	}
	
}
