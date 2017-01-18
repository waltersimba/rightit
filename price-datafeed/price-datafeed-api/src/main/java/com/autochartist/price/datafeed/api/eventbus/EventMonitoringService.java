package com.autochartist.price.datafeed.api.eventbus;

import com.autochartist.price.datafeed.api.event.Event;

public interface EventMonitoringService {

	<S extends EventSubscriber> void register(S subscriber);

	<S extends EventSubscriber> void unregister(S subscriber);

	<E extends Event> void post(E event);

}
