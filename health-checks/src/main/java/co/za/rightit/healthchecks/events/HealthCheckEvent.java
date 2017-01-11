package co.za.rightit.healthchecks.events;

public interface HealthCheckEvent {
	
	String getSystemName();
	
	boolean isHealthy();
	
	String getMessage() ;
}
