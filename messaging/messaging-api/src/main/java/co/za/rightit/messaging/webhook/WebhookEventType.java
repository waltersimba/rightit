package co.za.rightit.messaging.webhook;

public enum WebhookEventType {
	DUMMY_EVENT("DUMMY.EVENT", "A dummy event was created");
	
	private String name;
	private String description;
	
	private WebhookEventType(String name, String description) {
		this.name= name;
		this.description = description;
	}
	
}
