package com.autochartist.price.datafeed.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autochartist.price.datafeed.api.event.PriceChangeEvent;
import com.autochartist.price.datafeed.api.eventbus.EventMonitoringService;
import com.autochartist.price.datafeed.api.eventbus.EventSubscriber;
import com.autochartist.price.datafeed.api.util.Closeable;
import com.autochartist.price.datafeed.api.util.Openable;
import com.google.inject.Inject;

public class XPriceDataFeed implements EventSubscriber, PriceDataFeed, Openable, Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(XPriceDataFeed.class);
	private EventMonitoringService eventMonitoringService;

	@Inject
	public XPriceDataFeed(EventMonitoringService eventMonitoringService) {
		this.eventMonitoringService = eventMonitoringService;
	}

	@Override
	public void startDataStreaming() {
		LOGGER.debug("Starting data streaming...");
	}

	@Override
	public void stopDataStreaming() {
		LOGGER.debug("Stopping data streaming...");
	}

	@Override
	public void onDataStreamEvent() {
		// TODO: populate event information
		PriceChangeEvent event = null;
		eventMonitoringService.post(event);
	}

	@Override
	public void close() {
		eventMonitoringService.unregister(this);
		stopDataStreaming();
	}

	@Override
	public void open() {
		eventMonitoringService.register(this);
		stopDataStreaming();
	}

	@Override
	public String getSubscriberId() {
		return XPriceDataFeed.class.getName();
	}

}
