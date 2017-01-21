package co.za.rightit.healthchecks.api.notify.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import co.za.rightit.healthchecks.model.HealthCheck;
import co.za.rightit.healthchecks.mongo.HealthCheckRepository;

public class HealthCheckStateListener implements EventSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckStateListener.class);
	private final HealthCheckRepository repository;

	@Inject
	public HealthCheckStateListener(HealthCheckRepository repository) {
		this.repository = repository;
	}

	@Subscribe
	public void handle(HealthCheck healthCheck) {
		if (repository.updateHealthCheck(healthCheck))
			LOGGER.debug("Health check {} updated.", healthCheck.getName());
		else
			throw new RuntimeException("Failed to persist health check: " + healthCheck.getName());
	}

	@Override
	public String getSubscriberId() {
		return this.getClass().getSimpleName();
	}

}
