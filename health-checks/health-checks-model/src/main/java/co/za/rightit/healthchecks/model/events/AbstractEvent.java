package co.za.rightit.healthchecks.model.events;

public abstract class AbstractEvent implements Event {

	@Override
	public String getId() {
		return this.getClass().getSimpleName();
	}
	
}
