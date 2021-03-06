package co.za.rightit.messaging.web.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.za.rightit.messaging.web.model.EmailRequest;

@Path("/send")
public class MessagingResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingResource.class);

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/email")
	public Response email(EmailRequest request) {
		LOGGER.debug("received email request: {}", request);
		return Response.status(Status.ACCEPTED).build();
	}

}
