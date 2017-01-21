package co.za.rightit.healthchecks.api.commands;

import com.codahale.metrics.health.HealthCheck.Result;

import co.za.rightit.healthchecks.model.HealthCheck;

public interface HealthCheckCommand {
	Result execute(HealthCheck healthCheck);	
}
