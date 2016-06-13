package com.rightit.taxibook.security.shiro;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.google.inject.Inject;
import com.rightit.taxibook.security.JWTPrincipal;
import com.rightit.taxibook.service.authentication.TokenAuthenticationService;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;

@Singleton
public class JWTTokenFilter extends BasicHttpAuthenticationFilter {

	private static final String AUTH_SCHEME = "Bearer ";
		
	@Inject
	@Named("applicationName")
	private String applicationName;
	
	@Inject
	private TokenAuthenticationService tokenAuthenticationService;

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		final HttpServletRequest httpRequest = WebUtils.toHttp(request);
		final String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith(AUTH_SCHEME)) {
			throw new ApplicationRuntimeException("Missing or invalid Authorization header.");
		}
		final String token = authHeader.substring(AUTH_SCHEME.length());
		final JWTPrincipal jwtPrincipal = tokenAuthenticationService.authenticateToken(token);
		return new JWTAuthenticationToken(jwtPrincipal);
	}

	@Override
	public String getAuthzScheme() {
		return AUTH_SCHEME;
	}

	@Override
	public String getAuthcScheme() {
		return AUTH_SCHEME;
	}
	
	@Override
	public String getApplicationName() {
		return applicationName;
	}

}
