package co.za.rightit.healthchecks.api.modules;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import co.za.rightit.healthchecks.api.notify.EventService;
import co.za.rightit.healthchecks.api.notify.EventServiceImpl;
import co.za.rightit.healthchecks.api.notify.subscriber.SlackNotifier;
import co.za.rightit.healthchecks.api.slack.Slacker;

public class EventModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventServiceImpl.class);
	}
	
	@Provides
	@Singleton
	public ExecutorService executorService() {
		return Executors.newWorkStealingPool();
	}
		
	@Provides
	@Singleton
	public EventBus asyncEventBus(ExecutorService executorService) {
		return new AsyncEventBus(executorService);
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
	public SlackNotifier slackNotifier(EventService eventService, Slacker slacker) {
		final SlackNotifier notifier = new SlackNotifier(slacker);
		eventService.register(notifier);
		return notifier;
	}

}
