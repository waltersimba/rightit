package co.za.rightit.healthchecks.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck.Result;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import co.za.rightit.healthchecks.api.commands.HealthCheckCommand;
import co.za.rightit.healthchecks.api.commands.UrlHealthCheckCommand;
import co.za.rightit.healthchecks.model.HealthCheck;

public class HealthCheckExecutor {

	public static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckExecutor.class);
	private Map<HealthCheck.Type, HealthCheckCommand> commands;

	@SuppressWarnings("serial")
	public HealthCheckExecutor() {
		commands = new HashMap<HealthCheck.Type, HealthCheckCommand>() {
			{
				put(HealthCheck.Type.URL, new UrlHealthCheckCommand());
			}
		};
	}

	public ListenableFuture<Result> execute(HealthCheck healthCheck) {
		final ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1)); 
		try {
			return executor.submit(() -> {
				try {
					HealthCheckCommand command = commands.get(HealthCheck.Type.valueOf(healthCheck.getType()));
					if(command == null) {
						throw new RuntimeException(String.format("Command for health check type {} is not found.", healthCheck.getType()));
					}
					return Preconditions.checkNotNull(command.execute(healthCheck));
				} catch (Exception ex) {
					LOGGER.error("Failed to execute health check due to: {}", ex.getMessage());
					throw ex;
				}
			});
		} finally {
			executor.shutdown();
		}
	}

}
