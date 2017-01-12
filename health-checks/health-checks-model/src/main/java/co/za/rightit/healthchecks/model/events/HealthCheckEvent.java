package co.za.rightit.healthchecks.model.events;

public interface HealthCheckEvent {
	
	String getSystemName();
	
	boolean isHealthy();
	
	String getMessage();
	
}
