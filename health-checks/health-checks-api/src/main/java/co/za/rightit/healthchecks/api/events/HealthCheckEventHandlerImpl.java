package co.za.rightit.healthchecks.api.events;

import com.google.common.eventbus.EventBus;

import co.za.rightit.healthchecks.model.events.HealthCheckEvent;

public class HealthCheckEventHandlerImpl implements EventHandler<HealthCheckEvent> {

	private final EventBus eventBus;
	
	public HealthCheckEventHandlerImpl(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	@Override
	public void handle(HealthCheckEvent event) {
		eventBus.post(event);		
	}

}
