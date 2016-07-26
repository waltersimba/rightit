package co.za.rightit.taxibook.security;

import java.security.Principal;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class JWTPrincipal implements Principal {

	public static final JWTPrincipal UNAUTHENTICATED = new JWTPrincipal(null, "anonymous", null);
	
	private final String userId;
	
	private final String username;
	
	private final String role;
	
	public JWTPrincipal(String userId, String username, String role) {
		this.userId = userId;
		this.username = username;
		this.role = role;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String getName() {
		return getUsername();
	}

	public Set<String> getRoles() {
		return ImmutableSet.of(getRole());
	}
	
}
