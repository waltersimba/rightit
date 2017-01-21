package co.za.rightit.healthchecks.model;

public class NotificationChannel {
	
	private String type;
	
	private String destination;
	
	public NotificationChannel(String type, String destination) {
		this.type = type;
		this.destination = destination;
	}

	public String getType() {
		return type;
	}

	public String getDestination() {
		return destination;
	}
	
	public enum Type {
		SLACK("slack"),
		EMAIL("email"),
		SMS("sms"),
		WEB_HOOK("webhook");
		
		private String identifier;
		
		private Type(String identifier) {
			this.identifier = identifier;
		}
		
		public String getIdentifier() {
			return identifier;
		}
	}
	
}
