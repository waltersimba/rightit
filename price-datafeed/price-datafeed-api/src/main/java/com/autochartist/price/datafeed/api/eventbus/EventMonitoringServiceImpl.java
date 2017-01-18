package com.autochartist.price.datafeed.api.eventbus;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autochartist.price.datafeed.api.event.Event;
import com.autochartist.price.datafeed.api.util.Closeable;
import com.google.common.eventbus.AsyncEventBus;

public class EventMonitoringServiceImpl implements EventMonitoringService, Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventMonitoringServiceImpl.class);

	/**
	 * Dispatch event asynchronously.
	 */
	private final AsyncEventBus asyncEventBus;
	/**
	 * Handles events that could not be delivered.
	 */
	private final DeadEventSubscriber deadEventSubscriber;

	public EventMonitoringServiceImpl() {
		asyncEventBus = new AsyncEventBus(Executors.newCachedThreadPool(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = Executors.defaultThreadFactory().newThread(r);
				thread.setDaemon(true);
				thread.setName("EventMonitoringService pool");
				return thread;
			};
		}));
		deadEventSubscriber = new DeadEventSubscriber();
		asyncEventBus.register(deadEventSubscriber);
	}

	@Override
	public <S extends EventSubscriber> void register(S subscriber) {
		LOGGER.debug("registering subscriber {}", (subscriber != null ? subscriber.getSubscriberId() : "null"));
		asyncEventBus.register(subscriber);
	}

	@Override
	public <S extends EventSubscriber> void unregister(S subscriber) {
		LOGGER.debug("unregistering subscriber {}", (subscriber != null ? subscriber.getSubscriberId() : "null"));
		asyncEventBus.unregister(subscriber);
	}

	@Override
	public <E extends Event> void post(E event) {
		LOGGER.debug("posting event {}", (event != null ? event.getId() : "null"));
		asyncEventBus.post(event);
	}

	@Override
	public void close() {
		asyncEventBus.unregister(deadEventSubscriber);		
	}

}
