package co.za.rightit.healthchecks.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck.Result;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Inject;

import co.za.rightit.healthchecks.api.commands.HealthCheckCommand;
import co.za.rightit.healthchecks.api.commands.UrlHealthCheckCommand;
import co.za.rightit.healthchecks.api.notify.EventService;
import co.za.rightit.healthchecks.constants.HealthCheckConstants;
import co.za.rightit.healthchecks.model.HealthCheck;
import co.za.rightit.healthchecks.model.events.HealthCheckEvent;
import co.za.rightit.healthchecks.model.events.HealthCheckStateChangedEvent;

public class HealthCheckExecutor {

	public static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckExecutor.class);
	private final Map<HealthCheck.Type, HealthCheckCommand> commands;
	private final ExecutorService executor;
	private final EventService eventService;
	@Inject
	private HealthCheckStatus healthCheckStatus;

	@SuppressWarnings("serial")
	@Inject
	public HealthCheckExecutor(ExecutorService executor, EventService eventService) {
		this.executor = executor;
		this.eventService = eventService;
		commands = new HashMap<HealthCheck.Type, HealthCheckCommand>() {
			{
				put(HealthCheck.Type.URL, new UrlHealthCheckCommand());
			}
		};
	}

	public void execute(HealthCheck healthCheck) {
		final ListeningExecutorService listeningExecutor = MoreExecutors.listeningDecorator(executor); 
		final ListenableFuture<Result> result = listeningExecutor.submit(() -> {
			try {
				HealthCheckCommand command = commands.get(HealthCheck.Type.valueOf(healthCheck.getType()));
				if(command == null) {
					throw new RuntimeException(String.format("Command for health check type {} is not found.", 
							healthCheck.getType()));
				}
				return Preconditions.checkNotNull(command.execute(healthCheck));
			} catch (Exception ex) {
				LOGGER.error("Failed to execute health check due to: {}", ex.getMessage());
				throw ex;
			}
		});
		Futures.addCallback(result, new FutureCallbackHandler(healthCheck));
	}

	private class FutureCallbackHandler implements FutureCallback<Result> {

		private final HealthCheck healthCheck;

		public FutureCallbackHandler(HealthCheck healthCheck) {
			this.healthCheck = healthCheck;
		}

		@Override
		public void onSuccess(Result result) {
			healthCheckStatus.updated();
			if(isHealthCheckStatusChanged(result)) {
				healthCheck.put(HealthCheckConstants.HEALTHY, result.isHealthy());
				if(shouldSendNotification()) {
					eventService.post(new HealthCheckEvent(healthCheck.getName(), result.isHealthy(),result.getMessage()));
				}
				healthCheck.put(HealthCheckConstants.LAST_PING, new DateTime());
				eventService.post(new HealthCheckStateChangedEvent(healthCheck));
			}
		}

		@Override
		public void onFailure(Throwable thrown) {
			LOGGER.error("Failed to execute health check.", thrown);			
		}

		private boolean isHealthCheckStatusChanged(Result result) {
			boolean lastHealthy = healthCheck.getBoolean(HealthCheckConstants.HEALTHY);
			return lastHealthy != result.isHealthy();
		}

		private boolean shouldSendNotification() {
			DateTime lastPing = healthCheck.getDateTime(HealthCheckConstants.LAST_PING);
			int duration = healthCheck.getInt(HealthCheckConstants.INTERVAL) + healthCheck.getInt(HealthCheckConstants.GRACE);
			return !lastPing.plusSeconds(duration).isBeforeNow();
		}

	}

}
