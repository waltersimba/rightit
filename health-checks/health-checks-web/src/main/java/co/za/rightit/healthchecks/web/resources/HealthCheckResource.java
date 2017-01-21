package co.za.rightit.healthchecks.web.resources;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.za.rightit.healthchecks.model.HealthCheck;
import co.za.rightit.healthchecks.mongo.HealthCheckRepository;
import co.za.rightit.healthchecks.web.HealthCheckExecutor;

@Path("checks")
public class HealthCheckResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckResource.class);
	@Inject
	private HealthCheckRepository repository;
	@Inject
	private HealthCheckExecutor executor;
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public Response pingCheck(@PathParam("id") String healthCheckId) {
		LOGGER.debug("Pinging health check {} ...", healthCheckId);
		Optional<HealthCheck> healthCheckOptional = repository.getHealthCheck(healthCheckId);
		if(!healthCheckOptional.isPresent()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			executor.execute(healthCheckOptional.get());
		}
		catch(Exception ex) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok().build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response listChecks() {
		return Response.ok().build();
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createCheck() {
		return Response.ok().build();
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/pause/{id}")
	public Response pauseCheck(@PathParam("id") String healthCheckId) {
		return Response.ok().build();
	}
	
}
