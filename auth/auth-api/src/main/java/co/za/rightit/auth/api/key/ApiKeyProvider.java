package co.za.rightit.auth.api.key;

import co.za.rightit.auth.model.credential.ApiKey;

public interface ApiKeyProvider {
	ApiKey get(String username);
}
