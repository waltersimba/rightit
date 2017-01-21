package co.za.rightit.healthchecks.api.notify.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.builder.ToStringBuilder;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

/**
 * Intercept messages that failed to be delivered to any known subscriber.
 */
public class DeadEventSubscriber implements EventSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeadEventSubscriber.class);

	@Override
	public String getSubscriberId() {
		return this.getClass().getSimpleName();
	}

	@Subscribe
	public void handleDeadEvent(DeadEvent event) {
		LOGGER.error(event != null ? ToStringBuilder.reflectionToString(event.getEvent()) : "null object");
	}

}
