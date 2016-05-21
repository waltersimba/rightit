package com.rightit.taxibook.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import com.google.inject.Inject;
import com.rightit.taxibook.service.authentication.TokenAuthenticationService;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;

/**
 * Filter all incoming requests, look for possible token information and use
 * that to create and load a SecurityContext to request.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BearerTokenFilter implements ContainerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";

	@Inject
	private TokenAuthenticationService tokenAuthenticationService;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		final String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
			throw new ApplicationRuntimeException("Missing or invalid Authorization header.");
		}
		final String token = authHeader.substring(BEARER_PREFIX.length());
		final JWTPrincipal jwtPrincipal = tokenAuthenticationService.authenticateToken(token);
		requestContext.setSecurityContext(new BearerSecurityContext(jwtPrincipal));
	}

}
