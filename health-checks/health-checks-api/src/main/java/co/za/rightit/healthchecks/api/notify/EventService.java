package co.za.rightit.healthchecks.api.notify;

import co.za.rightit.healthchecks.api.notify.subscriber.EventSubscriber;
import co.za.rightit.healthchecks.model.events.Event;

public interface EventService {
	<S extends EventSubscriber> void register(S subscriber);
	<S extends EventSubscriber> void unregister(S subscriber);
	<E extends Event> void post(E event);
}
