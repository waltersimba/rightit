package com.autochartist.price.datafeed.api.event;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class PriceChangeEventListenerImpl implements PriceChangeEventListener {

	private final AsyncEventGateway<PriceChangeEvent> asyncEventGateway;

	@Inject
	public PriceChangeEventListenerImpl(final AsyncEventGateway<PriceChangeEvent> eventGateway) {
		this.asyncEventGateway = eventGateway;
	}

	@Override
	@Subscribe
	public void onPriceChangeEvent(PriceChangeEvent event) {
		asyncEventGateway.sendAsyncEvent(event);
	}

}
