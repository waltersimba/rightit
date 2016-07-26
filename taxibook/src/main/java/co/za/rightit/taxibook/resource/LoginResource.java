package co.za.rightit.taxibook.resource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.za.rightit.taxibook.service.authentication.AuthenticationToken;
import co.za.rightit.taxibook.service.authentication.LoginRequest;
import co.za.rightit.taxibook.service.authentication.LoginService;

@Path("session")
public class LoginResource {
	
	@Inject
	private LoginService loginService;
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response login(LoginRequest request) throws Throwable {
		CompletableFuture<AuthenticationToken> futureToken = loginService.login(request);
		AuthenticationToken authenticationToken = null;
		try {
			authenticationToken = futureToken.get();
		} catch (InterruptedException ex) {
			throw ex;
		} catch (ExecutionException ex) {
			throw ex.getCause();
		}
		return Response.ok(authenticationToken).build();
	}
}
