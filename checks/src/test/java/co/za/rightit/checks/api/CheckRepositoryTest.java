package co.za.rightit.checks.api;

import static co.za.rightit.checks.api.ChecksDataFactory.createCheckConfig;
import static co.za.rightit.checks.api.ChecksDataFactory.getChecksCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.jongo.MongoCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonProcessingException;	
import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.DuplicateKeyException;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.model.Node;

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
      getCollection().insert(createCheckConfig());
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
      Optional<CheckConfig> checkConfigFound = repository.getCheckByName("MemcachedCheck");
      assertTrue(checkConfigFound.isPresent());
      CheckConfig config = checkConfigFound.get();
      assertNotNull(config);
      assertEquals("MemcachedCheck", config.getName());
      assertEquals(2, config.getNodes().size());
   }

   @Test(expected = DuplicateKeyException.class)
   public void testShouldAvoidInsertingDuplicateCheckConfigNames() {
      System.out.println("testShouldAvoidInsertingDuplicateCheckConfigNames");
      getCollection().insert(createCheckConfig());
   }

   @Test
   public void testShouldPersistUpdatedCheckConfigToDB() {
      System.out.println("testShouldPersistUpdatedCheckConfigToDB");
      CheckConfig checkConfig = repository.getCheckByName("MemcachedCheck").get();
      List<Node> checkNodes = checkConfig.getNodes();
      Node checkNode = checkNodes.get(1);
      assertEquals("hostname", checkNode.getName());

      assertNull("lastNotified shouldn't be set", checkNode.get("lastNotified"));

      long lastNotified = new Date().getTime() / 1000;
      checkNode.put("lastNotified", lastNotified);

      assertNotNull("lastNotified is set", checkNode.get("lastNotified"));
      assertEquals(lastNotified, checkNode.get("lastNotified"));

      assertTrue("MemcachedCheck configuration should be persisted to DB", repository.updateCheck("MemcachedCheck", checkNodes));

      CheckConfig updatedCheckConfig = repository.getCheckByName("MemcachedCheck").get();
      Node updatedCheckNode = updatedCheckConfig.getNodes().get(1);
      assertEquals("hostname node should be updated in the DB", "hostname", updatedCheckNode.getName());
      assertEquals("lastNotified should be updated in the DB", lastNotified, updatedCheckNode.get("lastNotified"));
   }

   private MongoCollection getCollection() {
      if(checks == null) {
         checks = getChecksCollection();
      }
      return checks;
   }

}