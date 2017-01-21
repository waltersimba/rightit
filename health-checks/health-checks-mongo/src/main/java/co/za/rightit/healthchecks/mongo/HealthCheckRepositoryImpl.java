package co.za.rightit.healthchecks.mongo;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import co.za.rightit.healthchecks.model.HealthCheck;

public class HealthCheckRepositoryImpl implements HealthCheckRepository {
	
	private MongoCollection healthChecks;
	
	public HealthCheckRepositoryImpl(MongoCollection healthChecks) {
		this.healthChecks = healthChecks;
	}

	@Override
	public HealthCheck createHealthCheck(HealthCheck healthCheck) {
		healthChecks.insert(healthCheck);
		return healthCheck;
	}

	@Override
	public Optional<HealthCheck> getHealthCheck(String id) {
		HealthCheck healthCheck = healthChecks.findOne("{_id: #}", new ObjectId(id)).as(HealthCheck.class);
		return Optional.ofNullable(healthCheck);
	}

	@Override
	public boolean updateHealthCheck(HealthCheck healthCheck) {
		return healthChecks.save(healthCheck).wasAcknowledged();
	}

}
