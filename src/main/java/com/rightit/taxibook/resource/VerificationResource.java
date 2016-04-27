package com.rightit.taxibook.resource;

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

import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.service.token.EmailVerificationRequest;
import com.rightit.taxibook.service.token.VerificationTokenService;

@Path("verify")
public class VerificationResource {

	@Inject
	private VerificationTokenService verificationTokenService;

	@Path("tokens")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response sendEmailToken(EmailVerificationRequest request) throws Throwable {
		CompletableFuture<Optional<VerificationToken>> futureToken = verificationTokenService.generateEmailVerificationToken(request);
		Optional<VerificationToken> optionalToken = null;
		try {
			optionalToken = futureToken.get();
		} catch (InterruptedException ex) {
			throw ex;
		} catch (ExecutionException ex) {
			throw ex.getCause();
		}
		
		return Response.ok(optionalToken.get()).build();
	}
}
