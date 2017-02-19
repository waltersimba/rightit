package co.za.rightit.commons.event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class EventModule extends AbstractModule {

	@Override
	protected void configure() {
		final ExecutorService executor = Executors.newWorkStealingPool();
		final EventService eventService = new EventServiceImpl(new AsyncEventBus(executor));
		bind(EventService.class).toInstance(eventService);
		bind(DeadEventSubscriber.class);
		bindListener(Matchers.any(), new TypeListener() {
			@Override
			public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
				typeEncounter.register(new InjectionListener<I>() {
					@Override
					public void afterInjection(I instance) {
						if(EventSubscriber.class.isAssignableFrom(instance.getClass())) {
							final EventSubscriber eventSubscriber = (EventSubscriber) instance;
							eventService.register(eventSubscriber);
						}
					}
				});
			}			
		});
	}
	
}
