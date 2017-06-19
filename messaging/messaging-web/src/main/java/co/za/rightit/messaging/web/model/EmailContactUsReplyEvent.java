package co.za.rightit.messaging.web.model;

import com.google.common.base.Preconditions;

import co.za.rightit.commons.event.Event;

public class EmailContactUsReplyEvent implements Event, EmailRequestEvent {

	private String domain;
	private EmailContactUsReply reply;
	
	public EmailContactUsReplyEvent(String domain, EmailContactUsReply reply) {
		this.domain = Preconditions.checkNotNull(domain, "domain");
		this.reply = Preconditions.checkNotNull(reply, "reply");
	}
	
	public String getDomain() {
		return domain;
	}
	
	@SuppressWarnings("unchecked")
	public EmailContactUsReply getData() {
		return reply;
	}
	
	@Override
	public String getId() {
		return String.format("%s.%s-%s", getClass().getSimpleName(), domain, reply);
	}

}
