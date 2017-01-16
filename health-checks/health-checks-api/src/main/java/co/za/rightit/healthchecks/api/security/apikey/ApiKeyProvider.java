package co.za.rightit.healthchecks.api.security.apikey;

public interface ApiKeyProvider {
	ApiKey get(String username);
}
