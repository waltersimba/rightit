package com.rightit.taxibook.service.authentication;

public class AuthenticationToken {
	/**
	 * A access token is the result of an authorization process, and encapsulates
 	 * the authorization to access protected information.
	 */
	private final String accessToken;
	/**
	 * The refresh token is an optional token that can be used when the access
	 * token has expired.
	 */
	private final String refreshToken;
	
	public AuthenticationToken(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
	
}
