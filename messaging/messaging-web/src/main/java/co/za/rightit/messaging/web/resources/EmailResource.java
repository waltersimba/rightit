package co.za.rightit.messaging.web.resources;

import javax.ws.rs.Consumes;
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

import co.za.rightit.commons.event.EventService;
import co.za.rightit.messaging.web.model.EmailContactUsRequest;
import co.za.rightit.messaging.web.model.EmailContactUsRequestEvent;

@Path("/emails")
public class EmailResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailResource.class);
	@Inject
	private EventService eventService;

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("contact-us")
	public Response contactUs(@QueryParam("domain_name") String domainName, EmailContactUsRequest request) {
		LOGGER.info("received email contact us request: {}, domain={}", request, domainName);
		eventService.post(new EmailContactUsRequestEvent(domainName, request));
		return Response.status(Status.ACCEPTED).build();
	}

}
