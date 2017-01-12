package co.za.rightit.healthchecks.api.modules;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class EventBusModule extends AbstractModule {

	@Override
	protected void configure() {}
	
	@Provides
	@Singleton
	public AsyncEventBus asyncEventBus(ExecutorService executorService) {
		return new AsyncEventBus(executorService);
	}
	
	@Provides
	@Singleton
	public ExecutorService executorService() {
		return Executors.newWorkStealingPool();
	}

}
