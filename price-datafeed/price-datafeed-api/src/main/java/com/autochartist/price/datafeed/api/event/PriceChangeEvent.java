package com.autochartist.price.datafeed.api.event;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.autochartist.price.datafeed.api.model.OHLCVBarValue;

public class PriceChangeEvent implements Event {

	private static final long serialVersionUID = 1L;
	private String feed;
	private String symbol;
	private Integer interval;
	private DateTime date;
	private OHLCVBarValue ohlcv;

	public PriceChangeEvent() {
	}

	public PriceChangeEvent withFeed(String feed) {
		this.feed = feed;
		return this;
	}

	public PriceChangeEvent withSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	public PriceChangeEvent withInterval(Integer interval) {
		this.interval = interval;
		return this;
	}

	public PriceChangeEvent withDate(long timestamp) {
		this.date = new DateTime(date).withZone(DateTimeZone.UTC);
		return this;
	}

	public PriceChangeEvent withOHLCV(OHLCVBarValue olhcv) {
		this.ohlcv = olhcv;
		return this;
	}

	public String getFeed() {
		return feed;
	}

	public String getSymbol() {
		return symbol;
	}

	public Integer getInterval() {
		return interval;
	}

	public DateTime getDate() {
		return date;
	}

	public OHLCVBarValue getOHLCV() {
		return ohlcv;
	}

	@Override
	public Serializable getId() {
		return String.format("%s.%s.%s.%s", PriceChangeEvent.class.getSimpleName(), feed, symbol,
				Integer.toString(interval));
	}

}
