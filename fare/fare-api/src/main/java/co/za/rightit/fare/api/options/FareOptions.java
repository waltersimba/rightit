package co.za.rightit.fare.api.options;

import java.util.Currency;
import java.util.Locale;

import com.google.maps.model.TrafficModel;

public class FareOptions {

	private final String distanceMatrixApiKey;
	private final TrafficModel trafficModel;
	private final Double baseFee;
	private final Double perMinute;
	private final Double perKilometer;
	private final Double minimumFare;
	private final Currency currency;
	private final Locale locale;
	
	public FareOptions(Builder builder) {
		this.distanceMatrixApiKey = builder.distanceMatrixApiKey;
		this.trafficModel = builder.trafficModel;
		this.baseFee = builder.baseFee;
		this.perMinute = builder.perMinute;
		this.perKilometer = builder.perKilometer;
		this.minimumFare = builder.minimumFare;
		this.currency = builder.currency;
		this.locale = builder.locale;
	}
	
	public String getDistanceMatrixApiKey() {
		return distanceMatrixApiKey;
	}

	public TrafficModel getTrafficModel() {
		return trafficModel;
	}
	
	public Double getBaseFee() {
		return baseFee;
	}

	public Double getPerMinute() {
		return perMinute;
	}

	public Double getPerKilometer() {
		return perKilometer;
	}

	public Double getMinimumFare() {
		return minimumFare;
	}
	
	public Currency getCurrency() {
		return currency;
	}

	public Locale getLocale() {
		return locale;
	}

	public static Builder newBuilder() {
		return new Builder();
	}
	
	public final static class Builder {
		
		private String distanceMatrixApiKey;
		private TrafficModel trafficModel;
		private Double baseFee;
		private Double perMinute;
		private Double perKilometer;
		private Double minimumFare;
		private Currency currency;
		private Locale locale;
		
		public Builder withDistanceMatrixApiKey(String distanceMatrixApiKey) {
			this.distanceMatrixApiKey = distanceMatrixApiKey;
			return this;
		}
		
		public Builder withTrafficModel(TrafficModel trafficModel) {
			this.trafficModel = trafficModel;
			return this;
		}
		
		public Builder withBaseFee(Double baseFree) {
			this.baseFee = baseFree;
			return this;
		}
		
		public Builder withPerMinute(Double perMinute) {
			this.perMinute = perMinute;
			return this;
		}
		
		public Builder withPerKilometer(Double perKilometer) {
			this.perKilometer = perKilometer;
			return this;
		}
		
		public Builder withMinimunFare(Double minimumFare) {
			this.minimumFare = minimumFare;
			return this;
		}
		
		public Builder withCurrency(Currency currency) {
			this.currency = currency;
			return this;
		}
		
		public Builder withLocale(Locale locale) {
			this.locale = locale;
			return this;
		}
		
		public FareOptions build() {
			return new FareOptions(this);
		}
	}
}
