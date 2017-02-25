package co.za.rightit.auth.model.credential;

import com.google.common.base.Preconditions;

public class ApiCredential implements Credential<ApiKey> {

	private final ApiKey apiKey;
	
	public ApiCredential(ApiKey apiKey) {
		this.apiKey =  Preconditions.checkNotNull(apiKey);
	}
	
	@Override
	public String getType() {
		return CredentialType.API.getValue();
	}

	@Override
	public ApiKey getValue() {
		return apiKey;
	}

}
