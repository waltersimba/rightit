package co.za.rightit.commons.event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import co.za.rightit.commons.utils.CleanupCapable;

public class EventServiceImpl implements EventService, CleanupCapable {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

	private final EventBus channel;
	private final ExecutorService executor;
		
	public EventServiceImpl() {
		executor = Executors.newWorkStealingPool();
		this.channel = new AsyncEventBus(executor);
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
	public void cleanup() {
		executor.shutdown();		
	}
	
}
