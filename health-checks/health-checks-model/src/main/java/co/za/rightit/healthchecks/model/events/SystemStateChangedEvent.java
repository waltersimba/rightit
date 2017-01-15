package co.za.rightit.healthchecks.model.events;

public class SystemStateChangedEvent implements HealthCheckEvent {
	
	private final String systemName;
	private final boolean healthy;
	private final String message;
	
	public SystemStateChangedEvent(String systemName, boolean healthy, String message) {
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
		return "SystemStateChangedEvent [systemName=" + systemName + ", healthy=" + healthy + ", message=" + message
				+ "]";
	}
	
}
