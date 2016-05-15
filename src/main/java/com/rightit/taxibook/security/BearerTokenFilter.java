package com.rightit.taxibook.security;

import javax.annotation.Priority;
import javax.inject.Singleton;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

import com.google.inject.Inject;
import com.rightit.taxibook.service.authentication.TokenAuthenticationService;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * Filter all incoming requests, look for possible token information and use
 * that to create and load a SecurityContext to request.
 */
@Singleton
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BearerTokenFilter implements ResourceFilter, ContainerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";
	
	@Inject
	private TokenAuthenticationService tokenAuthenticationService;

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		final JWTPrincipal jwtPrincipal = authenticate(request);
		request.setSecurityContext(new BearerSecurityContext(jwtPrincipal));
		return request;
	}

	@Override
	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return null;
	}

	private JWTPrincipal authenticate(final ContainerRequest request) {
		final String authHeader = request.getHeaderValue(ContainerRequest.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) { 
            throw new ApplicationRuntimeException("Missing or invalid Authorization header."); 
        } 
		try {
			final String token = authHeader.substring(BEARER_PREFIX.length()); 
			return tokenAuthenticationService.authenticateToken(token);
		} catch(Exception ex) {
			//authentication failed
		}
		return JWTPrincipal.UNAUTHENTICATED;
	}
}
