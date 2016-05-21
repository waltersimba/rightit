package com.rightit.taxibook.security;

import java.security.Principal;

public class BearerSecurityContext implements javax.ws.rs.core.SecurityContext {

	private final JWTPrincipal principal;

	public BearerSecurityContext(JWTPrincipal jwtPrincipal) {
		this.principal = jwtPrincipal;
	}

	@Override
	public String getAuthenticationScheme() {
		return "BEARER";
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isSecure() {
		return true;
	}

	@Override
	public boolean isUserInRole(String roleString) {
		return roleString != null && roleString.equals(principal.getRole());
	}
}
