package co.za.rightit.geolocation.web.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.google.maps.model.TrafficModel;
import com.google.maps.model.TransitRoutingPreference;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import co.za.rightit.geolocation.model.FareQuery;
import co.za.rightit.geolocation.model.FareResponse;
import co.za.rightit.geolocation.model.FareResponse.Measurement;

@Path("/fare")
public class FareResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(FareResource.class);
	private static final GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCVw-9nXwjDzYd-w86VQAvUYr_mcfoI6L8");
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response distanceBetweenPoints(FareQuery query) {
		DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context)
				.origins(new LatLng(query.getFrom().getLat(), query.getFrom().getLng()))
				.destinations(new LatLng(query.getTo().getLat(), query.getTo().getLng()))
				.departureTime(DateTime.now())
				.mode(TravelMode.DRIVING)
				.units(Unit.METRIC)
				.transitRoutingPreference(TransitRoutingPreference.LESS_WALKING)
				.trafficModel(TrafficModel.BEST_GUESS);
		try {
			final DistanceMatrix distanceMatrix = request.await();
			final FareResponse response = new FareResponse();
			response.withOrigin(distanceMatrix.originAddresses[0]);
			response.withDestination(distanceMatrix.destinationAddresses[0]);
			for(int i = 0; i < distanceMatrix.rows.length; ++i) {
				final DistanceMatrixRow row = distanceMatrix.rows[i];
				for(int j = 0; j < row.elements.length; ++j) {
					DistanceMatrixElement element = row.elements[j];
					if(element.status.equals(DistanceMatrixElementStatus.OK)) {
						response.withDistance(new Measurement(element.distance.humanReadable, element.distance.inMeters));
						response.withDuration(new Measurement( element.duration.humanReadable,  element.duration.inSeconds));
						response.withCost(FareUtil.calculateCost(element.distance.inMeters, element.duration.inSeconds));
						break;
					}
				}
			}
			LOGGER.info("Result: {}", response);
			return Response.ok(response).build();
		} catch (Exception ex) {
			LOGGER.error("Failed to calculate distance.", ex);
			throw new WebApplicationException(ex);
		}
	}
	
}
