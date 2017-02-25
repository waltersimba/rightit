package co.za.rightit.auth.model.app;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.za.rightit.auth.model.credential.ApiKey;

public class CreateAppResponse {
	
	private String appName;
	private ApiKey credential;
	
	public CreateAppResponse(String appName, ApiKey credential) {
		this.appName = appName;
		this.credential = credential;
	}
	
	@JsonProperty(value = "app_name")
	public String getAppName() {
		return appName;
	}
	
	public ApiKey getCredential() {
		return credential;
	}
		
}
