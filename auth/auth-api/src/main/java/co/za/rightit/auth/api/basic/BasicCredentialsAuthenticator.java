package co.za.rightit.healthchecks.api.security.basic;

import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Preconditions;

import co.za.rightit.healthchecks.api.security.AuthenticationException;
import co.za.rightit.healthchecks.api.security.Authenticator;
import co.za.rightit.healthchecks.api.security.apikey.ApiKey;
import co.za.rightit.healthchecks.api.security.apikey.ApiKeyProvider;

public class BasicCredentialsAuthenticator implements Authenticator<BasicCredentials, String> {

	private final ApiKeyProvider provider;
	
	@Inject
	public BasicCredentialsAuthenticator(ApiKeyProvider provider) {
		this.provider = Preconditions.checkNotNull(provider);
	}
	
	@Override
	public Optional<String> authenticate(BasicCredentials credentials) throws AuthenticationException {
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

	    return Optional.of(key.getUsername());
	}

}
