package co.za.rightit.auth.model.basic;

import java.security.Principal;

public class JWTPrincipal implements Principal {
	
	private final String id;
	private final String subject;
	
	public JWTPrincipal(String id, String subject) {
		this.id = id;
		this.subject = subject;
	}
	
	public String getId() {
		return id;
	}

	public String getSubject() {
		return subject;
	}

	@Override
	public String getName() {
		return getSubject();
	}
	
}
