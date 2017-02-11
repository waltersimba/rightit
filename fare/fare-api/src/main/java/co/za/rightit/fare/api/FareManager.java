package co.za.rightit.fare.api;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.google.maps.model.TransitRoutingPreference;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import co.za.rightit.fare.api.exception.FareException;
import co.za.rightit.fare.api.options.FareOptions;
import co.za.rightit.geolocation.model.FareQuery;
import co.za.rightit.geolocation.model.FareResponse;
import co.za.rightit.geolocation.model.FareResponse.Measurement;

public class FareManager {
		
	private final GeoApiContext context;
	private final FareOptions options;
	private static final Logger LOGGER = LoggerFactory.getLogger(FareManager.class);
	
	public FareManager(FareOptions options) {
		this.options = options;
		this.context = new GeoApiContext().setApiKey(options.getDistanceMatrixApiKey());
	}
	
	public FareResponse calculateFare(FareQuery query) {
		Preconditions.checkNotNull(query, "query");
		if(!query.hasDepartureTime()) query.setDepartureTime(DateTime.now());
		LOGGER.debug("Calculating fare: {}", query.toString());
		DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context)
				.origins(new LatLng(query.getFrom().getLat(), query.getFrom().getLng()))
				.destinations(new LatLng(query.getTo().getLat(), query.getTo().getLng()))
				.departureTime(query.getDepartureTime())
				.mode(TravelMode.DRIVING)
				.units(Unit.METRIC)
				.transitRoutingPreference(TransitRoutingPreference.LESS_WALKING)
				.trafficModel(options.getTrafficModel());
		final FareResponse response = new FareResponse();
		try {
			DistanceMatrix distanceMatrix = request.await();
			response.withOrigin(distanceMatrix.originAddresses[0]);
			response.withDestination(distanceMatrix.destinationAddresses[0]);
			for(int i = 0; i < distanceMatrix.rows.length; ++i) {
				final DistanceMatrixRow row = distanceMatrix.rows[i];
				for(int j = 0; j < row.elements.length; ++j) {
					DistanceMatrixElement element = row.elements[j];
					if(element.status.equals(DistanceMatrixElementStatus.OK)) {
						response.withDistance(new Measurement(element.distance.humanReadable, element.distance.inMeters));
						response.withDuration(new Measurement( element.duration.humanReadable,  element.duration.inSeconds));
						response.withCost(calculateCost(element.distance.inMeters, element.duration.inSeconds));
						break;
					}
				}
			}
			return response;
		} catch (Exception ex) {
			throw new FareException("Failed to calculate fare", ex);
		}
	}
	
	public Measurement calculateCost(Long distanceInMeters, Long durationInSeconds) {
		final Long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds);
		final Double cost = Math.max(options.getMinimumFare(), options.getBaseFee() + options.getPerMinute() * minutes + (distanceInMeters / 1000)
				* options.getPerKilometer());
		final Locale locale = options.getLocale();
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        formatter.setCurrency(options.getCurrency());
		return new Measurement(formatter.format(cost), cost);
	}
	
}

