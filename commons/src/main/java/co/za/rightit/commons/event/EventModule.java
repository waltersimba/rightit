package co.za.rightit.commons.event;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import co.za.rightit.commons.utils.CleanupHandler;
import co.za.rightit.commons.utils.guice.TypeMatchers;

public class EventModule extends AbstractModule {

	private final EventServiceImpl eventService = new EventServiceImpl();
	
	@Override
	protected void configure() {
		bind(EventService.class).toInstance(eventService);
		bind(DeadEventSubscriber.class);
		bindListener(TypeMatchers.subclassesOf(EventSubscriber.class), new EventSubscriberListener());
		bindListener(TypeMatchers.subclassesOf(CleanupHandler.class), new CleanupHandlerListener());
	}
	
	private class EventSubscriberListener implements TypeListener {

		@Override
		public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> typeEncounter) {
			typeEncounter.register(new InjectionListener<I>() {
				@Override
				public void afterInjection(I instance) {
					if(instance instanceof EventSubscriber) {
						final EventSubscriber eventSubscriber = (EventSubscriber) instance;
						eventService.register(eventSubscriber);
					}
				}
			});
		}	
	}
	
	private class CleanupHandlerListener implements TypeListener {

		@Override
		public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> typeEncounter) {
			typeEncounter.register(new InjectionListener<I>() {
				@Override
				public void afterInjection(I instance) {
					if(instance instanceof CleanupHandler) {
						final CleanupHandler cleanup = (CleanupHandler) instance;
						cleanup.register(eventService);
					} 
				}
			});
		}
	}
	
}
