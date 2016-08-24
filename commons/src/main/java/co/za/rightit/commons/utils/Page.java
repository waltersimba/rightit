package co.za.rightit.commons.utils;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

	private final Pagination pagination;
	private final List<T> items = new ArrayList<>();

	public Page(List<T> items, Pagination pagination) {
		this.items.addAll(items);
		this.pagination = pagination;
	}

	public Pagination getPagination() {
		return pagination;
	} 

	public List<T> getItems() {
		return items;
	}

}
