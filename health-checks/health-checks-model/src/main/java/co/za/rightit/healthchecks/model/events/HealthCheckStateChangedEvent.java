package co.za.rightit.healthchecks.model.events;

import co.za.rightit.healthchecks.model.HealthCheck;

public class HealthCheckStateChangedEvent extends AbstractEvent {

	private final HealthCheck healthCheck;
	
	public HealthCheckStateChangedEvent(HealthCheck healthCheck) {
		this.healthCheck = healthCheck;
	}
	
	public HealthCheck getHealthCheck() {
		return healthCheck;
	}

}
