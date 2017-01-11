package co.za.rightit.healthchecks.api.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.Date;

import org.jongo.MongoCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.DuplicateKeyException;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import co.za.rightit.healthchecks.model.CheckConfig;
import co.za.rightit.healthchecks.model.Node;
import co.za.rightit.healthchecks.mongo.CheckRepository;
import co.za.rightit.healthchecks.mongo.CheckRepositoryImpl;
import co.za.rightit.healthchecks.util.Property;

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class CheckRepositoryTest extends AbstractModule {

	   @Override
	   protected void configure() {

	   }

	   @Provides
	   @Singleton
	   public CheckRepository checkRepository() {
	      return new CheckRepositoryImpl(getCollection());
	   }

	   @Unit
	   @Inject
	   private CheckRepository repository;

	   private static MongoCollection checks;

	   @Before
	   public void executeBeforeEachTest() throws UnknownHostException {
	      getCollection().insert(ChecksDataFactory.createCheckConfig());
	   }

	   @After
	   public void executeAfterEachTest() {
	      getCollection().drop();
	      checks = null;
	   }

	   @Test
	   public void testCheckConfigByNameDoesNotExist() {
	      System.out.println("testCheckConfigByNameDoesNotExist");
	      Optional<CheckConfig> checkOptional = repository.getCheckByName("checkNameDoesNotExist");
	      assertFalse(checkOptional.isPresent());
	   }

	   @Test
	   public void testShouldRetrieveCheckConfig() throws JsonProcessingException {
	      System.out.println("testShouldRetrieveCheckConfig");
	      //given
	      Optional<CheckConfig> checkConfigFound = repository.getCheckByName("MemcachedCheck");
	      //when
	      assertTrue(checkConfigFound.isPresent());
	      //then
	      CheckConfig config = checkConfigFound.get();
	      assertNotNull(config);
	      assertEquals("MemcachedCheck", config.getName());
	      assertEquals(2, config.getNodes().size());
	   }

	   @Test(expected = DuplicateKeyException.class)
	   public void testShouldAvoidInsertingDuplicateCheckConfigNames() {
	      System.out.println("testShouldAvoidInsertingDuplicateCheckConfigNames");
	      getCollection().insert(ChecksDataFactory.createCheckConfig());
	   }

	   @Test
	   public void testNodeIndex() {
	      System.out.println("testNodeIndex");
	      //given
	      CheckConfig checkConfig = repository.getCheckByName("MemcachedCheck").get();
	      //when
	      assertNotNull(checkConfig.getNode("admin"));
	      assertNotNull(checkConfig.getNode("hostname"));
	      //then
	      assertEquals(0, checkConfig.getNodeIndex("admin"));
	      assertEquals(1, checkConfig.getNodeIndex("hostname"));
	   }

	   @Test
	   public void testShouldPersistUpdatedCheckConfigNodesToDB() {
	      System.out.println("testShouldPersistUpdatedCheckConfigNodesToDB");
	      //given
	      CheckConfig checkConfig = repository.getCheckByName("MemcachedCheck").get();
	      Node checkNode = checkConfig.getNode("hostname").get();
	      assertEquals("hostname", checkNode.getName());
	      assertNull("lastNotified shouldn't be set", checkNode.get("lastNotified"));
	      //when
	      long lastNotified = new Date().getTime() / 1000;
	      checkNode.put("lastNotified", lastNotified);
	      assertNotNull("lastNotified is set", checkNode.get("lastNotified"));
	      assertEquals(lastNotified, checkNode.get("lastNotified"));
	      assertTrue("MemcachedCheck configuration should be persisted to DB", repository.updateCheck("MemcachedCheck", checkConfig.getNodes()));
	      //then
	      CheckConfig updatedCheckConfig = repository.getCheckByName("MemcachedCheck").get();
	      Node updatedCheckNode = updatedCheckConfig.getNode("hostname").get();
	      assertEquals("hostname node should be updated in the DB", "hostname", updatedCheckNode.getName());
	      assertEquals("lastNotified should be updated in the DB", lastNotified, updatedCheckNode.get("lastNotified"));
	   }

	   @Test
	   public void testShouldPersistUpdatedCheckNodePropertyToDB() {
	      System.out.println("testShouldPersistUpdatedCheckNodePropertyToDB");
	      //given
	      assertTrue(repository.updateNodeProperty("MemcachedCheck", 1, new Property<>("lastNotified", 1483602523L)));
	      //when
	      CheckConfig config = checks.findOne("{name:#}", "MemcachedCheck").as(CheckConfig.class);
	      assertEquals(2, config.getNodes().size());
	      //then
	      Optional<Node> nodeOptional = config.getNode("hostname");
	      assertTrue(nodeOptional.isPresent());
	      String lastNotified = String.valueOf(nodeOptional.get().get("lastNotified"));
	      assertEquals(Long.parseLong("1483602523"), Long.parseLong(lastNotified));
	   }

	   private MongoCollection getCollection() {
	      if(checks == null) {
	         checks = ChecksDataFactory.getChecksCollection();
	      }
	      return checks;
	   }

	}