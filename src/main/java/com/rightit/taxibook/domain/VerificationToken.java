package com.rightit.taxibook.domain;

import java.util.UUID;

import org.joda.time.DateTime;

public class VerificationToken extends DomainObject {

	private static final int DEFAULT_EXPIRY_TIME_IN_MINS = 60 * 24; //24 hours
	
	private String token;
	
	private String userId;
	
	private DateTime expires;
	
	private VerificationTokenType tokenType;
	
	private boolean verified;
	
	public VerificationToken() {
		this(null, null);
	}
	
	public VerificationToken(String userId, VerificationTokenType tokenType) {
		this(userId, tokenType, DEFAULT_EXPIRY_TIME_IN_MINS);
	}
	
	public VerificationToken(String userId, VerificationTokenType tokenType, int expiraryTimeInMinutes) {
		super("token");
		this.userId = userId;
		this.tokenType = tokenType;
		this.token = UUID.randomUUID().toString();
		this.expires = calculateExpiryDate(expiraryTimeInMinutes);
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

	public static int getDefaultExpiryTimeInMins() {
		return DEFAULT_EXPIRY_TIME_IN_MINS;
	}

	public boolean hasExpired() {
		return expires.isBeforeNow();
	}
	
	private DateTime calculateExpiryDate(int expiraryTimeInMinutes) {
		return new DateTime().plusMinutes(expiraryTimeInMinutes);
	}
		
	@Override
	public String toString() {
		return "VerificationToken [token=" + token + ", userId=" + userId + ", expires=" + expires + ", tokenType="
				+ tokenType + ", verified=" + verified + "]";
	}



	public enum VerificationTokenType {
		RESET_PASSWORD,
		EMAIL_VERIFICATION
	}
}
