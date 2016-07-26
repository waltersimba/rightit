package co.za.rightit.taxibook.security.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import co.za.rightit.taxibook.security.JWTPrincipal;

public class JWTAuthenticationToken implements AuthenticationToken {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private JWTPrincipal principal;

	public JWTAuthenticationToken(JWTPrincipal principal) {
		this.principal = principal;
	}
	
	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public Object getCredentials() {
		return principal.getUsername();
	}
	
}
