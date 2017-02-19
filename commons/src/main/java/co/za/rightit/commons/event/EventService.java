package co.za.rightit.commons.event;

public interface EventService {
	<S extends EventSubscriber> void register(S subscriber);
	<S extends EventSubscriber> void unregister(S subscriber);
	<E extends Event> void post(E event);
}
