package co.za.rightit.healthchecks.api.modules;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import co.za.rightit.healthchecks.api.notify.EventService;
import co.za.rightit.healthchecks.api.notify.EventServiceImpl;
import co.za.rightit.healthchecks.api.notify.subscriber.DeadEventSubscriber;
import co.za.rightit.healthchecks.api.notify.subscriber.EventSubscriber;
import co.za.rightit.healthchecks.api.notify.subscriber.HealthCheckStateListener;
import co.za.rightit.healthchecks.api.notify.subscriber.SlackNotifier;
import co.za.rightit.healthchecks.api.slack.Slacker;

public class EventModule extends AbstractModule {

	@Override
	protected void configure() {
		final ExecutorService executor = Executors.newWorkStealingPool();
		bind(ExecutorService.class).toInstance(executor);
		final EventService eventService = new EventServiceImpl(new AsyncEventBus(executor));
		bind(EventService.class).toInstance(eventService);
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
		bind(DeadEventSubscriber.class);
		bind(HealthCheckStateListener.class);
	}

	@Provides
	@Singleton
	public Slacker slacker() {
		return new Slacker("services/T2P74FSUC/B3Q9FSLTU/1GkfjfcbzPschHhLYfkXnwya")
				.withScreenName("healthchecks-bot")
				.withChannel("#health-checks");
	}

	@Provides
	@Singleton
	public SlackNotifier slackNotifier(Slacker slacker) {
		return new SlackNotifier(slacker);
	}
	
}
