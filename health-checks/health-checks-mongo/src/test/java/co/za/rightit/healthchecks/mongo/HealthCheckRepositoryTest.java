package co.za.rightit.healthchecks.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.WriteResult;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;

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
	      //nothing
	   }

	   @After
	   public void executeAfterEachTest() {
	      getCollection().drop();
	      healthchecks = null;
	   }

	   @Test
	   public void testCanInsertHealthCheckToDB() {
	      System.out.println("testCanInsertHealthCheckToDB");
	      //given
	      HealthCheck healthCheck = HealthChecksDataFactory.createHealthCheck();
	      assertTrue(healthCheck.getId() == null);
	      //when
	      HealthCheck insertedHealthCheck = repository.createHealthCheck(healthCheck);
	      //then
	      assertNotNull(insertedHealthCheck.getId());
	   }

	   @Test
	   public void testCanRetrieveHealthCheckFromDB() {
	      System.out.println("testCanRetrieveHealthCheckFromDB");
	      //given
	      HealthCheck healthCheck = HealthChecksDataFactory.createHealthCheck();
	      HealthCheck insertedHealthCheck = repository.createHealthCheck(healthCheck);
	      ///then
	      Optional<HealthCheck> healthCheckFound = repository.getHealthCheck(insertedHealthCheck.getId());
	      assertTrue(healthCheckFound.isPresent());
	      assertEquals(healthCheck.getId(), healthCheckFound.get().getId());
	   }

	   private MongoCollection getCollection() {
	      if(healthchecks == null) {
	         healthchecks = ChecksDataFactory.getHealthChecksCollection();
	      }
	      return healthchecks;
	   }

	}