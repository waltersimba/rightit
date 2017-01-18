package com.autochartist.price.datafeed.api.event;

/**
 * 
 * Interface for sending events asynchronously.
 *
 * @param <E>
 */
public interface AsyncEventGateway<E extends Event> {
	void sendAsyncEvent(E event);
}
