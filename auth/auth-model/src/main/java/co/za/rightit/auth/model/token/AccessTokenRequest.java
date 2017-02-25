package co.za.rightit.auth.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class AccessTokenRequest {
	
	private String clientId;
	private String secret;
	/**
	 * Token grant type.
	 */
	private final String grantType;
	
	public AccessTokenRequest(String grantType) {
		this.grantType = Preconditions.checkNotNull(grantType, "grantType");
	}
	
	public AccessTokenRequest withClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}
	
	public AccessTokenRequest withSecret(String secret) {
		this.secret = secret;
		return this;
	}
		
	@JsonProperty(value="client_id")
	public String getClientId() {
		return clientId;
	}
	
	public String getSecret() {
		return secret;
	}

	@JsonProperty(value="grant_type")
	public String getGrantType() {
		return grantType;
	}
		
}
