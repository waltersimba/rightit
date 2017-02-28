package co.za.rightit.messaging.web.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.za.rightit.messaging.web.model.CreateWebhookRequest;

@Path("/webhooks")
public class WebhookResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailResource.class);

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	/**
	 * Subscribes a webhook listener to events
	 * @param request
	 * @return 
	 */
	public Response create(CreateWebhookRequest request) {
		LOGGER.debug("received create webhook request: {}", request.toString());
		return Response.status(Status.ACCEPTED).build();
	}
	
	@GET
	@Path("{webhook_id}")
	/**
	 * Show details for a webhook, by ID.
	 * @param webhookId
	 * @return
	 */
	public Response getWebhook(@PathParam("webhook_id") String webhookId) {
		return Response.ok().build();
	}
	
	@GET
	/**
	 * Lists all webhooks
	 * @return
	 */
	public Response listWebhooks() {
		return Response.ok().build();
	}
	
	@PUT
	@Path("{webhook_id}")
	/**
	 * Updates a webhook, by ID.
	 * @return
	 */
	public Response updateWebhook(@PathParam("webhook_id") String webhookId) {
		return Response.ok().build();
	}
	
	@DELETE
	@Path("{webhook_id}")
	/**
	 * Deletes a webhook, by ID.
	 * @param webhookId
	 * @return
	 */
	public Response deleteWebhook(@PathParam("webhook_id") String webhookId) {
		return Response.noContent().build();
	}
	
}
