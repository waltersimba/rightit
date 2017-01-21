package co.za.rightit.healthchecks.api.notify.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;

import co.za.rightit.healthchecks.api.slack.SlackMessage.Color;
import co.za.rightit.healthchecks.api.slack.Slacker;
import co.za.rightit.healthchecks.model.events.HealthCheckEvent;

public class SlackNotifier implements EventSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotifier.class);
	private final Slacker slacker;
	
	public SlackNotifier(Slacker slacker) {
		this.slacker = slacker;
	}
	
	@Subscribe
	public void handle(HealthCheckEvent event) {
		Preconditions.checkArgument(event != null, "HealthCheckEvent cannot be null");
		LOGGER.debug("Sending event to slack: {}" ,event.toString());
		String message = "Component health status changed:\n\n" + Preconditions.checkNotNull(event.getMessage(), "Message cannot be null");
		slacker.sendSlackMessage(message, event.isHealthy() ? Color.GOOD : Color.DANGER);
	}

	@Override
	public String getSubscriberId() {
		return this.getClass().getSimpleName();
	}

}
