package co.za.rightit.healthchecks.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.joda.time.DateTime;
import org.jongo.MongoCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import co.za.rightit.healthchecks.constants.HealthCheckConstants;
import co.za.rightit.healthchecks.model.HealthCheck;
import co.za.rightit.healthchecks.mongo.util.ChecksDataFactory;
import co.za.rightit.healthchecks.mongo.util.HealthChecksDataFactory;

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class HealthCheckRepositoryTest extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	public HealthCheckRepository healthCheckRepository() {
		return new HealthCheckRepositoryImpl(getCollection());
	}

	@Unit
	@Inject
	private HealthCheckRepository repository;

	private static MongoCollection healthchecks;

	@Before
	public void executeBeforeEachTest() {
		// nothing
	}

	@After
	public void executeAfterEachTest() {
		getCollection().drop();
		healthchecks = null;
	}

	@Test
	public void testCanInsertHealthCheckToDB() {
		System.out.println("testCanInsertHealthCheckToDB");
		// given
		HealthCheck healthCheck = HealthChecksDataFactory.createHealthCheck();
		assertTrue(healthCheck.getId() == null);
		// when
		HealthCheck insertedHealthCheck = repository.createHealthCheck(healthCheck);
		// then
		assertNotNull(insertedHealthCheck.getId());
	}

	@Test
	public void testCanRetrieveHealthCheckFromDB() {
		System.out.println("testCanRetrieveHealthCheckFromDB");
		// given
		HealthCheck healthCheck = HealthChecksDataFactory.createHealthCheck();
		HealthCheck insertedHealthCheck = repository.createHealthCheck(healthCheck);
		/// then
		Optional<HealthCheck> healthCheckFound = repository.getHealthCheck(insertedHealthCheck.getId());
		assertTrue(healthCheckFound.isPresent());
		assertEquals(healthCheck.getId(), healthCheckFound.get().getId());
	}

	@Test
	public void testCanUpdateHealthCheckToDB() {
		System.out.println("testCanRetrieveHealthCheckFromDB");
		// given
		HealthCheck healthCheck = HealthChecksDataFactory.createHealthCheck();
		healthCheck = repository.createHealthCheck(healthCheck);
		// when
		final DateTime updatedAt = new DateTime();
		healthCheck.put(HealthCheckConstants.LAST_PING, updatedAt);
		healthCheck.put(HealthCheckConstants.HEALTHY, false);
		assertTrue(repository.updateHealthCheck(healthCheck));
		// then
		Optional<HealthCheck> healthCheckOptional = repository.getHealthCheck(healthCheck.getId());
		assertTrue(healthCheckOptional.isPresent());
		HealthCheck updatedHealthCheck = healthCheckOptional.get();
		assertEquals(updatedAt, updatedHealthCheck.getDateTime(HealthCheckConstants.LAST_PING));
		assertFalse(updatedHealthCheck.getBoolean(HealthCheckConstants.HEALTHY));
	}

	private MongoCollection getCollection() {
		if (healthchecks == null) {
			healthchecks = ChecksDataFactory.getHealthChecksCollection();
		}
		return healthchecks;
	}

}