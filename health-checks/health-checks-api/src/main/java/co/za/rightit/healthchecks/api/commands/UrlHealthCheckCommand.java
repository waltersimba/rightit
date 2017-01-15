package co.za.rightit.healthchecks.api.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck.Result;

import co.za.rightit.healthchecks.api.UrlHealthCheck;
import co.za.rightit.healthchecks.constants.HealthCheckConstants;
import co.za.rightit.healthchecks.model.HealthCheck;

public class UrlHealthCheckCommand implements HealthCheckCommand {

	public static final Logger LOGGER = LoggerFactory.getLogger(UrlHealthCheckCommand.class);

	@Override
	public Result execute(HealthCheck healthCheck) {
		String url = healthCheck.getString(HealthCheckConstants.TARGET_URL);
		if (url == null) {
			throw new IllegalArgumentException(
					String.format("Target URL for \"%s\" is missing.", healthCheck.getName()));
		}
		return new UrlHealthCheck(url).execute();
	}

}
