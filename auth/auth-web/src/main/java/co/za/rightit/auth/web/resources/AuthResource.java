package co.za.rightit.auth.web.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import co.za.rightit.auth.api.token.TokenService;
import co.za.rightit.auth.model.token.AccessTokenRequest;

@Path("/")
public class AuthResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);
	@Inject
	private Provider<AccessTokenRequest> accessTokenRequestProvider;
	@Inject
	private TokenService tokenService;
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Path("/app/token")
	public Response getAccessToken() {
		try {
			return Response.ok(tokenService.getAccessToken(accessTokenRequestProvider.get())).build();
		} catch(Exception ex) {
			LOGGER.debug("Failed to get access token", ex);
			return Response.status(Status.FORBIDDEN).build();
		}
	}

}
