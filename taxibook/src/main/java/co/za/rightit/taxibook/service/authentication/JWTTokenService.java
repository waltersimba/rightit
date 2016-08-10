package co.za.rightit.taxibook.service.authentication;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.za.rightit.commons.exceptions.ApplicationRuntimeException;
import co.za.rightit.commons.utils.FailedCompletableFutureBuilder;
import co.za.rightit.taxibook.domain.User;
import co.za.rightit.taxibook.security.JWTPrincipal;
import co.za.rightit.taxibook.validation.exception.AuthenticationException;
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
	public CompletableFuture<String> generateToken(User user) {
		LOGGER.info("Generating JWT token for user with email address {}...", user.getEmailAddress());
		final CompletableFuture<String> future = new CompletableFuture<>();
		try {
			final JWTPrincipal jwtPrincipal = new JWTPrincipal(user.getEmailAddress(), user.getId().toString(), user.getRole().toString());
			String generatedToken = createJWT(UUID.randomUUID().toString(), jwtPrincipal, TimeUnit.MINUTES.toMillis(TTL_IN_MINUTES));
			future.complete(generatedToken);
			LOGGER.info("Token generated for user with email address {}", user.getEmailAddress());
		} catch(Exception ex) {
			String errorMessage = String.format("Failed to generate token for %s: %s", user.getEmailAddress(), ex.getMessage());
			LOGGER.error(errorMessage);
			return new FailedCompletableFutureBuilder<String>().build(new ApplicationRuntimeException(errorMessage));
		}
		return future;		
	}

	@Override
	public JWTPrincipal authenticateToken(String token) {
		LOGGER.info("Authenticating JWT token: {}", token);
		JWTPrincipal jwtPrincipal = null;
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(DatatypeConverter.parseBase64Binary(getApplicationSecret()))
					.parseClaimsJws(token).getBody();
			jwtPrincipal = buildPrincipal(claims);
			LOGGER.info("Token authenticated for : {}", jwtPrincipal.getUsername());
		} catch(ExpiredJwtException ex) {
			LOGGER.error("Failed to authenticate token due to expiry: {}", ex.getMessage());
			throw new AuthenticationException("The access token provided has expired: " + ex.getMessage());
		} catch(RuntimeException ex) {
			LOGGER.error("Failed to verify token: {}", ex.getMessage());
			throw new AuthenticationException("Failed to verify token: " + ex.getMessage());
		}	
		return jwtPrincipal;
	}
	
	private JWTPrincipal buildPrincipal(Claims claims) {
		String subject = claims.getSubject();
		String userId = (String) claims.get(USER_ID_ATTRIBUTE);
		String role = (String) claims.get(ROLE_ATTRIBUTE);
		return new JWTPrincipal(userId, subject, role);
	}

	private String createJWT(String id, JWTPrincipal jwtPrincipal, long ttlInMillis) {
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
				.setSubject(jwtPrincipal.getUsername())
				.claim(USER_ID_ATTRIBUTE, jwtPrincipal.getUserId())
				.claim(ROLE_ATTRIBUTE, jwtPrincipal.getRole())
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
