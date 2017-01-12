package co.za.rightit.healthchecks.api.events;

public interface EventHandler<T> {
	
	void handle(T event);
	
}
