package com.autochartist.price.datafeed.api.event;

public interface PriceChangeEventListener {
	void onPriceChangeEvent(PriceChangeEvent event);
}
