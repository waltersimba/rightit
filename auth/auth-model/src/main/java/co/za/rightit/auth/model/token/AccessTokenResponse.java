package co.za.rightit.auth.model.token;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessTokenResponse {
	
	/**
	 * Scopes expressed in the form of resource URL endpoints, space-delimited and case-sensitive.
	 */
	private String scope;
	private String accessToken;
	/**
	 * Type of token issued.
	 */
	private String tokenType;
	/**
	 * Lifetime of the access token, in seconds.
	 */
	private DateTime expiresIn;
	
	public AccessTokenResponse withScope(String scope) {
		this.scope = scope;
		return this;
	} 
	
	public AccessTokenResponse withAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}
	
	public AccessTokenResponse withTokenType(String tokenType) {
		this.tokenType = tokenType;
		return this;
	}
	
	public AccessTokenResponse withExpireIn(DateTime expiresIn) {
		this.expiresIn = expiresIn;
		return this;
	}
	
	public String getScope() {
		return scope;
	}
	
	@JsonProperty(value="access_token")
	public String getAccessToken() {
		return accessToken;
	}
	
	@JsonProperty(value="token_type")
	public String getTokenType() {
		return tokenType;
	}
	
	@JsonProperty(value="expires_in")
	public DateTime getExpiresIn() {
		return expiresIn;
	}
		
}
