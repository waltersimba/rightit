package co.za.rightit.messaging.webhook;

import com.google.common.base.Preconditions;

public class WebhookEvent {
	
	private String name;
	private String description;
	private String status;

	public WebhookEvent(String name, String description, String status) {
		this.name = Preconditions.checkNotNull(name, "name");
		this.description = Preconditions.checkNotNull(description, "description");
		this.status = Preconditions.checkNotNull(status, "status");
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getStatus() {
		return status;
	}
	
}
