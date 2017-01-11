package co.za.rightit.healthchecks.events;

import com.google.common.eventbus.EventBus;

public class EventHandlerImpl implements EventHandler {

	private final EventBus eventBus;
	
	public EventHandlerImpl(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	@Override
	public void handle(HealthCheckEvent event) {
		eventBus.post(event);		
	}

}
