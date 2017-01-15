package co.za.rightit.healthchecks.web.resources;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import com.codahale.metrics.health.HealthCheck.Result;

import co.za.rightit.healthchecks.api.events.EventHandler;
import co.za.rightit.healthchecks.constants.HealthCheckConstants;
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
	@Inject
	private EventHandler<Result> eventHandler;
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public Response pingCheck(@PathParam("id") String healthCheckId) {
		Optional<HealthCheck> healthCheckOptional = repository.getHealthCheck(healthCheckId);
		if(!healthCheckOptional.isPresent()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			final HealthCheck healthCheck = healthCheckOptional.get();
			final ListenableFuture<Result> result = executor.execute(healthCheck);
			Futures.addCallback(result, new FutureCallback<Result>() {

				@Override
				public void onSuccess(Result result) {
					healthCheck.put(HealthCheckConstants.LAST_PING, new DateTime());
					DateTime lastPing = healthCheck.getDateTime(HealthCheckConstants.LAST_PING);
					int duration = healthCheck.getInt(HealthCheckConstants.INTERVAL) + healthCheck.getInt(HealthCheckConstants.GRACE);
					boolean lastHealthy = healthCheck.getBoolean(HealthCheckConstants.HEALTHY);
					if(lastHealthy != result.isHealthy() && !lastPing.plusSeconds(duration).isBeforeNow()) {
						healthCheck.put(HealthCheckConstants.HEALTHY, result.isHealthy());
						//send notification
						//persist health check to database
						//execute in parallel
					}
				}
				
				@Override
				public void onFailure(Throwable thrown) {
					LOGGER.error(thrown.getMessage());
				}
			});
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
