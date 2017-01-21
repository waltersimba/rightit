package co.za.rightit.healthchecks.mongo.modules;

import org.jongo.Jongo;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import co.za.rightit.healthchecks.mongo.HealthCheckRepository;
import co.za.rightit.healthchecks.mongo.HealthCheckRepositoryImpl;

public class HealthCheckModule extends AbstractModule {

	@Override
	protected void configure() {
	}
	
	@Provides
	@Singleton
	public HealthCheckRepository healthCheckRepository(Jongo jongo) {
		return new HealthCheckRepositoryImpl(jongo.getCollection("checks"));
	}
	
}
