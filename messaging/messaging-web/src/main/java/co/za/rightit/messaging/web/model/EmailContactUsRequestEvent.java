package co.za.rightit.messaging.web.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import co.za.rightit.commons.event.Event;

public class EmailContactUsRequestEvent implements Event {

	private final EmailContactUsRequest request;
	private final String domainName;
	
	public EmailContactUsRequestEvent(String domainName, EmailContactUsRequest request) {
		this.request = Preconditions.checkNotNull(request, "request");
		this.domainName = domainName;
	}
	
	public EmailContactUsRequest getRequest() {
		return request;
	}
	
	public String getDomainName() {
		return domainName;
	}
	
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("domain", domainName)
				.add("to", request.getTo())
				.toString();
	}
	
	@Override
	public String getId() {
		return String.format("%s.%s-%s", getClass().getSimpleName(), request.getTo(), domainName);
	}

}
