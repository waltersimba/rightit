package co.za.rightit.commons.utils;

import java.util.List;
import java.util.stream.Collectors;

import co.za.rightit.commons.utils.Page;
import co.za.rightit.commons.utils.Pageable;
import co.za.rightit.commons.utils.Pagination;

public class Paginator {

	private Paginator() {
		throw new AssertionError("Can't instantiate: " + Paginator.class.getSimpleName());
	}

	public static <T> Page<T> paginate(List<T> items, Pageable pageable) {
		List<T> paginatedList = items.stream()
				.skip(pageable.getOffset())
				.limit(pageable.getLimit())
				.collect(Collectors.toList());
		Pagination pagination = new Pagination(pageable.getPageNumber(), items.size(), pageable.getLimit());
		return new Page<T>(paginatedList, pagination);
	}

}
