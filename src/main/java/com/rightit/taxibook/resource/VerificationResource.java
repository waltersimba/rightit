package com.rightit.taxibook.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rightit.taxibook.service.token.EmailVerificationRequest;
import com.rightit.taxibook.service.token.VerificationTokenService;

@Path("verify")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class VerificationResource {

	@Inject
	VerificationTokenService verificationTokenService;

	@Path("tokens")
	@POST
	public Response sendEmailToken(EmailVerificationRequest request) {
		verificationTokenService.generateEmailVerificationToken(request);
		return Response.ok().build();
	}
}
