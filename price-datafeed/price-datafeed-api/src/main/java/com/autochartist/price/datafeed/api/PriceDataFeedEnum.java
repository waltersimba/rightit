package com.autochartist.price.datafeed.api;

public enum PriceDataFeedEnum {

	X_FEED("x-feed");

	private String name;

	private PriceDataFeedEnum(String name) {
		this.name = name;
	}

	public String getFeedName() {
		return name;
	}
}
