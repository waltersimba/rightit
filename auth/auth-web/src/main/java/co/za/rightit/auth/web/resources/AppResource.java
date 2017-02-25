package co.za.rightit.auth.web.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import co.za.rightit.auth.api.app.ApplicationService;
import co.za.rightit.auth.model.app.CreateAppRequest;
import co.za.rightit.auth.model.app.CreateAppResponse;

@Path("/app")
public class AppResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppResource.class);
	
	@Inject
	private ApplicationService applicationService;
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response createApp(@QueryParam("app_name") String appName) {
		if(appName == null) {
			return Response.status(Status.BAD_REQUEST).build();
		} else {
			try {
				CreateAppResponse response = applicationService.createApp(new CreateAppRequest(appName));
				return Response.status(Status.CREATED).entity(response).build();
			} catch(Exception ex) {
				return Response.serverError().build();
			}
		}
	}
	
}
