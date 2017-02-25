package co.za.rightit.auth.api;

import java.nio.charset.StandardCharsets;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.BaseEncoding;

import co.za.rightit.auth.model.basic.UsernamePassword;

/**
 * Parse Authorization header and create BasicCredentials. Authenticate
 * credentials using BasicCredentialsAuthenticator
 */
public class BasicCredentialAuthFilter<P extends Principal> extends AuthFilter<UsernamePassword, P> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicCredentialAuthFilter.class);

	private UsernamePassword getCredentials(String header) {
		if (header == null) {
			return null;
		}
		final int space = header.indexOf(' ');
		if (space <= 0) {
			return null;
		}
		final String method = header.substring(0, space);
		if (!prefix.equalsIgnoreCase(method)) {
			return null;
		}
		final String decoded;
		try {
			decoded = new String(BaseEncoding.base64().decode(header.substring(space + 1)), StandardCharsets.UTF_8);
		} catch (IllegalArgumentException ex) {
			LOGGER.warn("Error decoding credentials", ex);
			return null;
		}
		// Decoded credentials is 'username:password'
		final int i = decoded.indexOf(':');
		if (i <= 0) {
			return null;
		}
		final String username = decoded.substring(0, i);
		final String password = decoded.substring(i + 1);
		return new UsernamePassword(username, password);
	}

	public static class Builder<P extends Principal> extends AuthFilterBuilder<UsernamePassword, P, BasicCredentialAuthFilter<P>> {
		@Override
		protected BasicCredentialAuthFilter<P> newInstance() {
			return new BasicCredentialAuthFilter<>();
		}
	}

}
