package co.za.rightit.commons.utils;

public class Pageable {

	private final int limit;
	private final int offset;

	public Pageable(int offset, int limit) {
		if (limit == 0) {
			throw new IllegalArgumentException("limit cannot be 0");
		}
		this.offset = offset;
		this.limit = limit;
	}

	/**
	 * The limit for this object. The limit determines the maximum amount of
	 * results to return.
	 * 
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * The offset for this pagination object. The offset determines what index
	 * (0 index) to start retrieving results from.
	 *
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * The current page number this object represents.
	 * 
	 * @return the page number
	 */
	public int getPageNumber() {
		if (offset < limit)
			return 1;
		return (offset / limit) + 1;
	}

}
