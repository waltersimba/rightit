package co.za.rightit.auth.model.credential;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiKey {

	private final String username;
	private final String secret;
	
	public ApiKey(String username, String secret) {
		this.username = username;
		this.secret = secret;
	}

	@JsonProperty(value = "key")
	public String getUsername() {
		return username;
	}

	public String getSecret() {
		return secret;
	}
	
}
