package co.za.rightit.messaging.web.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import co.za.rightit.commons.event.Event;

public class EmailContactUsRequestEvent implements Event, EmailRequestEvent {

	private final EmailContactUsRequest request;
	private final String domain;
	
	public EmailContactUsRequestEvent(String domain, EmailContactUsRequest request) {
		this.request = Preconditions.checkNotNull(request, "request");
		this.domain = Preconditions.checkNotNull(domain, "domain");
	}
	
	@SuppressWarnings("unchecked")
	public EmailContactUsRequest getData() {
		return request;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("domain", domain)
				.add("to", request.getTo())
				.toString();
	}
	
	@Override
	public String getId() {
		return String.format("%s.%s-%s", getClass().getSimpleName(), request.getTo(), domain);
	}

}
