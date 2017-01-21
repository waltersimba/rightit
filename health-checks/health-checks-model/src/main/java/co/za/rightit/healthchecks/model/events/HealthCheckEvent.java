package co.za.rightit.healthchecks.model.events;

public class HealthCheckEvent extends AbstractEvent implements SystemChangedEvent {
	
	private final String systemName;
	private final boolean healthy;
	private final String message;
	
	public HealthCheckEvent(String systemName, boolean healthy, String message) {
		this.systemName = systemName;
		this.healthy = healthy;
		this.message = message;
	}
	
	@Override
	public String getSystemName() {
		return systemName;
	}
	
	@Override
	public boolean isHealthy() {
		return healthy;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "HealthCheckEvent [systemName=" + systemName + ", healthy=" + healthy + ", message=" + message
				+ "]";
	}

}
