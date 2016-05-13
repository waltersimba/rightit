package com.rightit.taxibook.service.authentication;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.security.UserInfo;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;
import com.rightit.taxibook.validation.exception.AuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTTokenService implements TokenAuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTTokenService.class);
	private static final String APPLICATION_SECRET = "taxibook_secret";
	private static final String ISSUER = "http://taxibook.co.za";
	private static final String ROLE_ATTRIBUTE = "role";
	private static final String USER_ID_ATTRIBUTE = "userId";
	private static final Long TTL_IN_MINUTES = 15L;

	@Override
	public String generateToken(User user) {
		LOGGER.info("Generating token for user with email address {}...", user.getEmailAddress());
		String generatedToken = null;
		try {
			final UserInfo userInfo = new UserInfo(user.getEmailAddress(), user.getId().toString(), user.getRole().toString());
			generatedToken = createJWT(UUID.randomUUID().toString(), userInfo, TimeUnit.MINUTES.toMillis(TTL_IN_MINUTES));
			LOGGER.info("Token generated for user with email address {}", user.getEmailAddress());
		} catch(Exception ex) {
			LOGGER.error("Failed to generate token for user with email address {}: {}", user.getEmailAddress(), ex.getMessage());
			throw new ApplicationRuntimeException(String.format("Failed to generate token for %s: %s",
					user.getEmailAddress(), 
					ex.getMessage()));
		}
		return generatedToken;		
	}

	@Override
	public UserInfo authenticateToken(String token) {
		LOGGER.info("Authenticating token: {}", token);
		UserInfo userInfo = null;
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(DatatypeConverter.parseBase64Binary(getApplicationSecret()))
					.parseClaimsJws(token).getBody();
			String subject = claims.getSubject();
			String userId = (String) claims.get(USER_ID_ATTRIBUTE);
			String role = (String) claims.get(ROLE_ATTRIBUTE);
			userInfo = new UserInfo(userId, subject, role);
			LOGGER.info("Token authenticated for subject: {}", subject);
		} catch(ExpiredJwtException ex) {
			LOGGER.error("Failed to authenticate token due to expiry: {}", ex.getMessage());
			throw new AuthenticationException("Token has expired: " + ex.getMessage());
		} catch(RuntimeException ex) {
			LOGGER.error("Failed to verify token: {}", ex.getMessage());
			throw new AuthenticationException("Failed to verify token: " + ex.getMessage());
		}	
		return userInfo;
	}

	private String createJWT(String id, UserInfo userInfo, long ttlInMillis) {
		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowInMillis = System.currentTimeMillis();
		Date now = new Date(nowInMillis);
		// We will sign our JWT with our application secret
		byte[] applicationSecretBytes = DatatypeConverter.parseBase64Binary(getApplicationSecret());
		Key signingKey = new SecretKeySpec(applicationSecretBytes, signatureAlgorithm.getJcaName());

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder()
				.setId(id)
				.setIssuedAt(now)
				.setNotBefore(now)
				.setSubject(userInfo.getUsername())
				.claim(USER_ID_ATTRIBUTE, userInfo.getUserId())
				.claim(ROLE_ATTRIBUTE, userInfo.getRole())
				.setIssuer(ISSUER)
				.signWith(signatureAlgorithm, signingKey);

		// if TTL has been specified, let's add the expiration
		if (ttlInMillis >= 0) {
			builder.setExpiration(new Date(nowInMillis + ttlInMillis));
		}

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}
	
	private String getApplicationSecret() {
		return APPLICATION_SECRET;
	}
	
}
