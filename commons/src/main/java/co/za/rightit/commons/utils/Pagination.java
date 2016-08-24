package co.za.rightit.commons.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pagination {

	private int currentPage;
	private int totalItems;
	private int totalPages;
	private int itemsPerPage;

	public Pagination(int currentPage, int totalItems, int itemsPerPage) {
		this.currentPage = currentPage;
		this.totalItems = totalItems;
		this.itemsPerPage = itemsPerPage;
		this.totalPages = (totalItems - 1 ) / itemsPerPage + 1;
	}

	@JsonProperty("current_page")
	public int getCurrentPage() {
		return currentPage;
	}

	@JsonProperty("total_items")
	public int getTotalItems() {
		return totalItems;
	}

	@JsonProperty("total_pages")
	public int getTotalPages() {
		return totalPages;
	}

	@JsonProperty("items_per_page")
	public int getItemsPerPage() {
		return itemsPerPage;
	}

}
