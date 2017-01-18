package com.autochartist.price.datafeed.api;

public interface PriceDataFeed {

	void onDataStreamEvent();

	void startDataStreaming();

	void stopDataStreaming();
}
