package co.za.rightit.messaging.web.model;

import com.google.common.base.Preconditions;

import co.za.rightit.commons.event.Event;

public class EmailRequestEvent implements Event {

	private final EmailRequest request;
	
	public EmailRequestEvent(EmailRequest request) {
		this.request = Preconditions.checkNotNull(request, "request");
	}
	
	public EmailRequest getEmailRequest() {
		return request;
	}
	
	@Override
	public String getId() {
		return String.format("%s.%s", getClass().getSimpleName(), request.getTo());
	}

}
