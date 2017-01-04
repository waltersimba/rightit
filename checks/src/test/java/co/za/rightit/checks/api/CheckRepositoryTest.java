package co.za.rightit.checks.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fakemongo.Fongo;
import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.DB;
import com.mongodb.DuplicateKeyException;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.model.CheckNodeConfig;

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class CheckRepositoryTest extends AbstractModule {

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
		List<CheckNodeConfig> checkNodes = checkConfig.getNodes();
		CheckNodeConfig checkNode = checkNodes.get(1);
		assertEquals("hostname", checkNode.getName());

		assertNull("lastNotified shouldn't be set", checkNode.getValue("lastNotified"));
		
		long lastNotified = new Date().getTime() / 1000;
		checkNode.putValue("lastNotified", lastNotified);
		
		assertNotNull("lastNotified is set", checkNode.getValue("lastNotified"));
		assertEquals(lastNotified, checkNode.getValue("lastNotified"));
		
		assertTrue("MemcachedCheck configuration should be persisted to DB", repository.updateCheck("MemcachedCheck", checkNodes));
		
		CheckConfig updatedCheckConfig = repository.getCheckByName("MemcachedCheck").get();
		CheckNodeConfig updatedCheckNode = updatedCheckConfig.getNodes().get(1);
		assertEquals("hostname node should be updated in the DB", "hostname", updatedCheckNode.getName());
		assertEquals("lastNotified should be updated in the DB", lastNotified, updatedCheckNode.getValue("lastNotified"));
	}

	@Override
	protected void configure() {
		
	}

	@Provides
	@Singleton
	public CheckRepository checkRepository() {
		return new CheckRepositoryImpl(getCollection());
	}
	
	private static CheckConfig createCheckConfig() {
		/*
		 * CheckConfig JSON Representation	
		 		{
				  "name": "MemcachedCheck",
				  "nodes": [
				    {
				      "name": "admin",
				      "values": {
				        "email1": "sysadmin@examle.com",
				        "debug": false,
				        "mailServer": "127.0.0.1",
				        "contactsFile": "/path/to/contacts.ini"
				      }
				    },
				    {
				      "name": "hostname",
				      "values": {
				        "retries": 3,
				        "port": "11211",
				        "notifyEvery": 20,
				        "host": "127.0.0.1",
				        "errorMessage": "Some error message",
				        "type": "TCPCheck",
				        "executeOnFailureHost": "hostname",
				        "timeout": 10,
				        "executeOnFailureCommand": "service memcached restart"
				      }
				    }
				  ]
				}
		*/
		
		return new CheckConfig().withName("MemcachedCheck").withNodes(createCheckNodeConfig());
	}

	@SuppressWarnings("serial")
	private static List<CheckNodeConfig> createCheckNodeConfig() {
		List<CheckNodeConfig> nodes = new ArrayList<>();
		nodes.add(new CheckNodeConfig().withName("admin").withValues(new HashMap<String, Object>(){
			{
				put("email1", "sysadmin@examle.com");
				put("mailServer", "127.0.0.1");
				put("contactsFile", "/path/to/contacts.ini");
				put("debug", Boolean.FALSE);
			}
		}));
		nodes.add(new CheckNodeConfig().withName("hostname").withValues(new HashMap<String, Object>(){
			{
				put("type", "TCPCheck");
				put("host", "127.0.0.1");
				put("port", "11211");
				put("timeout", 10);
				put("retries", 3);
				put("executeOnFailureHost", "hostname");
				put("executeOnFailureCommand", "service memcached restart");
				put("notifyEvery", 20);
				put("errorMessage", "Some error message");
			}
		}));
		
		return nodes;
	}
	
	private MongoCollection getCollection() {
		if(checks == null) {
			Fongo fongo = new Fongo("mongo in-memory server");
			DB db = fongo.getDB("config");
			Jongo jongo = new Jongo(db);
			checks = jongo.getCollection("checks");
			checks.ensureIndex("{name:1}", "{unique: true}");
		}
		return checks;
	}
	
}
