package co.za.rightit.auth.api.basic;

import java.util.Optional;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;

import co.za.rightit.auth.api.AuthenticationException;
import co.za.rightit.auth.api.Authenticator;
import co.za.rightit.auth.model.basic.JWTPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

public class JWTAuthenticator implements Authenticator<String, JWTPrincipal> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticator.class);
	
	private Provider<String> secretProvider;
	
	public JWTAuthenticator(Provider<String> secretProvider) {
		this.secretProvider = secretProvider;
	}
	
	@Override
	public Optional<JWTPrincipal> authenticate(String token) throws AuthenticationException {
		LOGGER.info("authenticating JWT token: {}", token);
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(DatatypeConverter.parseBase64Binary(secretProvider.get()))
					.parseClaimsJws(token).getBody();
			LOGGER.info("token authenticated successfully");
			return Optional.of(new JWTPrincipal((String)claims.get("client_id"), claims.getSubject()));
		} catch(ExpiredJwtException ex) {
			LOGGER.error("Failed to authenticate token due to expiry: {}", ex.getMessage());
			throw new AuthenticationException("The access token provided has expired: " + ex.getMessage());
		} catch(RuntimeException ex) {
			LOGGER.error("Failed to verify token: {}", ex.getMessage());
			throw new AuthenticationException("Failed to verify token: " + ex.getMessage());
		}	
	}

}
