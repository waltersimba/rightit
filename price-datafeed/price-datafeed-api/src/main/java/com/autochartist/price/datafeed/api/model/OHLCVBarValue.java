package com.autochartist.price.datafeed.api.model;

public class OHLCVBarValue {

	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private Integer volume;

	public OHLCVBarValue(Double open, Double high, Double low, Double close, Integer volume) {
		super();
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}

	public Double getOpen() {
		return open;
	}

	public Double getHigh() {
		return high;
	}

	public Double getLow() {
		return low;
	}

	public Double getClose() {
		return close;
	}

	public Integer getVolume() {
		return volume;
	}

}
