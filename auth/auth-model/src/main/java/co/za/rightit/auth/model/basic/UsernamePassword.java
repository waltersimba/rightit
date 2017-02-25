package co.za.rightit.healthchecks.api.security.basic;

public class BasicCredentials {
	
	private final String username;
	private final String password;
	
	public BasicCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
}
