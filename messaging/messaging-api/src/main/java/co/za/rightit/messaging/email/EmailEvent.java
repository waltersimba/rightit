package co.za.rightit.messaging.email;

import com.google.common.base.Preconditions;

import co.za.rightit.commons.event.Event;

public class EmailEvent implements Event {
	
	private final EmailMessage message;
	
	public EmailEvent(EmailMessage message) {
		this.message = Preconditions.checkNotNull(message, "message");
	}
	
	public EmailMessage getMessage() {
		return message;
	}

	@Override
	public String getId() {
		return String.format("%s.%s", getClass().getSimpleName(), message.getSenderEmail());
	}
	
}
	
	
