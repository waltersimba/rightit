package co.za.rightit.healthchecks.api.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

public final class GuiceHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GuiceHelper.class);

	public static void shutdownExecutorService(Injector injector) {
		try {
			if (injector != null) {
				LOGGER.debug("Begin shutdown of ExecutorService..");
				Provider<ExecutorService> executorServiceProvider = injector.getInstance(
						Key.get(new TypeLiteral<Provider<ExecutorService>>() {})
				);
				ExecutorService service = executorServiceProvider.get();
				LOGGER.debug("ExecutorService " + service.toString() + " is about to die..");
				service.shutdownNow();
				service.awaitTermination(10000, TimeUnit.MILLISECONDS);
				LOGGER.debug("ExecutorService " + service.toString() + " is dead.");
			}
		} catch (InterruptedException ex) {
			LOGGER.warn("Swallowing InterruptedException during shutdown", ex);
		}
	}

}
