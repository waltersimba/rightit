package co.za.rightit.catalog.domain;

import java.util.NoSuchElementException;

public enum StockStatus {
	IN_STOCK("In Stock"),
	OUT_OF_STOCK("Out Of Stock");
	
	private String title;
	
	StockStatus(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getValue() {
		return toString();
	}
	
	public static StockStatus fromTitle(String title) {
		for(StockStatus availability : values()) {
			if(availability.title.equals(title)) {
				return availability;
			}
		}
		throw new NoSuchElementException("Availability not found: " + title);
	}
	
};
