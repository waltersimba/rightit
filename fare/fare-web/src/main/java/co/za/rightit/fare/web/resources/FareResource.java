package co.za.rightit.fare.web.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.za.rightit.fare.api.FareManager;
import co.za.rightit.fare.api.exception.FareException;
import co.za.rightit.fare.api.options.FareOptionsDefault;
import co.za.rightit.fare.web.cors.CORS;
import co.za.rightit.geolocation.model.FareQuery;
import co.za.rightit.geolocation.model.FareResponse;

@CORS
@Path("/estimate")
public class FareResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(FareResource.class);
	private final FareManager fareManager = new FareManager(FareOptionsDefault.getFareOptions());
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response estimate(FareQuery query) {
		try {
			FareResponse fareResponse = fareManager.calculateFare(query);
			LOGGER.debug("Fare estimate: {}",fareResponse.toString());
			return Response.ok(fareResponse).build();
		} catch (FareException ex) {
			LOGGER.error("Failed to calculate fare.", ex);
			throw new WebApplicationException(ex);
		}
	}
	
}
