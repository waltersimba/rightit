package co.za.rightit.healthchecks.model.events;

public interface SystemChangedEvent extends Event {
	
	String getSystemName();
	
	boolean isHealthy();
	
	String getMessage();
	
}
