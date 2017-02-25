package co.za.rightit.auth.api.basic;

import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Preconditions;

import co.za.rightit.auth.api.AuthenticationException;
import co.za.rightit.auth.api.Authenticator;
import co.za.rightit.auth.api.key.ApiKeyProvider;
import co.za.rightit.auth.model.basic.BasicPrincipal;
import co.za.rightit.auth.model.basic.UsernamePassword;
import co.za.rightit.auth.model.credential.ApiKey;

/**
 * Convert HTTP basic credentials into an API key
 */
public class BasicCredentialsAuthenticator implements Authenticator<UsernamePassword, BasicPrincipal> {

	private final ApiKeyProvider provider;
	
	@Inject
	public BasicCredentialsAuthenticator(ApiKeyProvider provider) {
		this.provider = Preconditions.checkNotNull(provider);
	}
	
	@Override
	public Optional<BasicPrincipal> authenticate(UsernamePassword credentials) throws AuthenticationException {
		Preconditions.checkNotNull(credentials);

	    String username = credentials.getUsername();
	    String secret = credentials.getPassword();

	    ApiKey key = provider.get(username);
	    if (key == null) {
	      return Optional.empty();
	    }

	    if (!secret.equals(key.getSecret())) {
	      return Optional.empty();
	    }

	    return Optional.of(new BasicPrincipal(key.getUsername()));
	}

}
