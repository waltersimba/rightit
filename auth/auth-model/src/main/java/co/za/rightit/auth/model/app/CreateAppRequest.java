package co.za.rightit.auth.model.app;

import com.google.common.base.Preconditions;

public class CreateAppRequest {
	
	private String appName;
	
	public CreateAppRequest(String appName) {
		this.appName = Preconditions.checkNotNull(appName, "appName");
	}
	
	public String getAppName() {
		return appName;
	}
	
}
