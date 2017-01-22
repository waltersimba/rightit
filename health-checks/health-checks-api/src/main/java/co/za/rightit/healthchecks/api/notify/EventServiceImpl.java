package co.za.rightit.healthchecks.api.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import co.za.rightit.healthchecks.api.notify.subscriber.DeadEventSubscriber;
import co.za.rightit.healthchecks.api.notify.subscriber.EventSubscriber;
import co.za.rightit.healthchecks.api.util.Closeable;
import co.za.rightit.healthchecks.model.events.Event;

public class EventServiceImpl implements EventService, Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

	private final EventBus channel;
	/**
	 * Handles events that could not be delivered.
	 */
	private final DeadEventSubscriber deadEventSubscriber;

	public EventServiceImpl(final EventBus channel) {
		this.channel = channel;
		deadEventSubscriber = new DeadEventSubscriber();
		channel.register(deadEventSubscriber);
	}

	@Override
	public <S extends EventSubscriber> void register(S subscriber) {
		LOGGER.debug("registering subscriber {}", (subscriber != null ? subscriber.getSubscriberId() : "null"));
		channel.register(subscriber);
	}

	@Override
	public <S extends EventSubscriber> void unregister(S subscriber) {
		LOGGER.debug("unregistering subscriber {}", (subscriber != null ? subscriber.getSubscriberId() : "null"));
		channel.unregister(subscriber);
	}

	@Override
	public <E extends Event> void post(E event) {
		LOGGER.debug("posting event {}", (event != null ? event.getId() : "null"));
		channel.post(event);
	}

	@Override
	public void close() throws Exception {
		channel.unregister(deadEventSubscriber);
	}
}
