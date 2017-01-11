package co.za.rightit.healthchecks.events;

public interface EventHandler<T> {
	
	void handle(T event);
	
}
