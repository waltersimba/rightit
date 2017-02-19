package co.za.rightit.messaging.email;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import co.za.rightit.commons.event.EventSubscriber;

public class EmailEventSubscriber implements EventSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailEventSubscriber.class);
	private ProducerTemplate producer;
	
	@Inject
	public EmailEventSubscriber(CamelContext camelContext) {
		Preconditions.checkNotNull(camelContext, "camelContext");
		producer = camelContext.createProducerTemplate();
	}
	
	@Subscribe
	public void onEmailEvent(EmailEvent evt) {
		LOGGER.debug("Proccessing email event for \"{}\"", evt.getMessage().getRecipients());
		Preconditions.checkNotNull(evt, "evt");
		Preconditions.checkNotNull(evt.getMessage(), "message");
		producer.asyncSendBody("seda:email", evt.getMessage());
	}

	@Override
	public String getSubscriberId() {
		return getClass().getName();
	}
	
}
