package com.rightit.taxibook.domain;

import java.util.UUID;

import org.joda.time.DateTime;

public class VerificationToken extends DomainObject {

	private String token;
	
	private String userId;
	
	private DateTime expires;
	
	private VerificationTokenType tokenType;
	
	private boolean verified;
	
	public VerificationToken() {
		this(null, null, null);
	}
		
	public VerificationToken(String userId, VerificationTokenType tokenType, DateTime expires) {
		super("token");
		this.userId = userId;
		this.tokenType = tokenType;
		this.token = UUID.randomUUID().toString();
		this.expires = expires;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public DateTime getExpires() {
		return expires;
	}

	public void setExpires(DateTime expires) {
		this.expires = expires;
	}

	public VerificationTokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(VerificationTokenType tokenType) {
		this.tokenType = tokenType;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean hasExpired() {
		return expires.isBeforeNow();
	}
		
	public enum VerificationTokenType {
		REFRESH_TOKEN,
		RESET_PASSWORD,
		EMAIL_VERIFICATION
	}
}
