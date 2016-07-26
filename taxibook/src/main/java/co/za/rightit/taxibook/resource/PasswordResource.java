package co.za.rightit.taxibook.resource;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.za.rightit.taxibook.domain.VerificationToken;
import co.za.rightit.taxibook.service.verify.EmailBasedRequest;
import co.za.rightit.taxibook.service.verify.PasswordRequest;
import co.za.rightit.taxibook.service.verify.VerificationTokenService;

@Path("password")
public class PasswordResource {

	@Inject
	private VerificationTokenService verificationTokenService;
	
	@Path("tokens")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response sendResetPasswordToken(EmailBasedRequest request) throws Throwable {
		CompletableFuture<Optional<VerificationToken>> futureToken = verificationTokenService.generateResetPasswordToken(request);
		Optional<VerificationToken> optionalToken = null;
		try {
			optionalToken = futureToken.get();
		} catch (InterruptedException ex) {
			throw ex;
		} catch (ExecutionException ex) {
			throw ex.getCause();
		}
		return Response.status(Response.Status.CREATED).entity(optionalToken.get()).build();
	}
	
	@Path("reset")
	@POST
	public Response resetPassword(PasswordRequest request) throws Throwable {
		CompletableFuture<Optional<VerificationToken>> futureToken = verificationTokenService.resetPassword(request);
		try {
			futureToken.get();
		} catch (InterruptedException ex) {
			throw ex;
		} catch (ExecutionException ex) {
			throw ex.getCause();
		}
		return Response.ok().build();
	}
	
}
