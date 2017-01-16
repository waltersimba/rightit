package co.za.rightit.healthchecks.api.security.apikey;

public class ApiKey {

	private final String username;
	private final String secret;
	
	public ApiKey(String username, String secret) {
		this.username = username;
		this.secret = secret;
	}

	public String getUsername() {
		return username;
	}

	public String getSecret() {
		return secret;
	}
	
}
