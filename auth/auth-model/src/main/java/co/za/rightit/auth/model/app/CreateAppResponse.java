package co.za.rightit.auth.model.app;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.za.rightit.auth.model.credential.ApiKey;

public class CreateAppResponse {
	
	private String appName;
	private ApiKey credentials;
	
	public CreateAppResponse(String appName, ApiKey credentials) {
		this.appName = appName;
		this.credentials = credentials;
	}
	
	@JsonProperty(value = "app_name")
	public String getAppName() {
		return appName;
	}
	
	public ApiKey getCredentials() {
		return credentials;
	}
		
}
