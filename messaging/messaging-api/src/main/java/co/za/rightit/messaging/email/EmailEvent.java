package co.za.rightit.messaging.email;

public class EmailEvent {
	
	private final EmailMessage message;
	
	public EmailEvent(EmailMessage message) {
		this.message = message;
	}
	
	public EmailMessage getMessage() {
		return message;
	}
	
}
	
	
