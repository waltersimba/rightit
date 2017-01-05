package co.za.rightit.checks.api;

import static co.za.rightit.checks.api.ChecksDataFactory.createCheckConfig;
import static co.za.rightit.checks.api.ChecksDataFactory.getChecksCollection;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.model.Node;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class MongoPreferencesTest extends AbstractModule {

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
	private MongoPreferences objectUnderTest;
	private Optional<CheckConfig> configOptional;

	@Before
	public void executeBeforeEachTest() throws UnknownHostException {
		getCollection().insert(createCheckConfig());
		configOptional = repository.getCheckByName("MemcachedCheck");
		objectUnderTest = new MongoPreferences(configOptional.get(), createPreferenceChangeListener());
	}

	@After
	public void executeAfterEachTest() {
		getCollection().drop();
		checks = null;
		configOptional = null;
		objectUnderTest = null;
	}

	@Test
	public void testMongoPreferencesIsInstantiated() {
		System.out.println("testMongoPreferencesIsInstantiated");
		assertNotNull(objectUnderTest);
		assertNotNull(repository);
		assertTrue("CheckConfig cannot be null", configOptional.isPresent());
	}

	@Test
	public void testShouldContainAdminNode() throws BackingStoreException {
		System.out.println("testShouldContainNode");
		Preferences node = objectUnderTest.node("admin");
		assertNotNull("Should contain \"admin\" node", node);
		assertEquals("Node name should be \"admin\"", "admin", node.name());
		assertEquals("Node should have no children", 0, node.childrenNames().length);
		assertEquals("Node key length should be 4", 4, node.keys().length);
	}

	@Test
	public void testShouldPersistUpdatedNodeToDB() {
		System.out.println("testShouldPersistUpdatedNodeToDB");
		Preferences node = objectUnderTest.node("hostname");
		assertNotNull("Should contain \"hostname\" node", node);
		//update lastNotified property
		long lastNotified = new Date().getTime() / 1000;
		node.putLong("lastNotified", lastNotified);
		assertEquals("lastNotified should be updated", lastNotified, node.getLong("lastNotified", 0L));
		CheckConfig updatedConfig = repository.getCheckByName("MemcachedCheck").get();
		Node updatedNode = updatedConfig.getNode("hostname").get();
		assertEquals("lastNotified should be persisted to DB", Long.toString(lastNotified), String.valueOf(updatedNode.get("lastNotified")));
	}

	private PreferenceChangeListener createPreferenceChangeListener() {
		return new PreferenceChangeListener() {

			@Override
			public void preferenceChange(PreferenceChangeEvent evt) {
				System.out.println(String.format("[%s] node changed: %s=%s", evt.getNode().name(), evt.getKey(), evt.getNewValue()));
				CheckConfig config = configOptional.get();
				System.out.println("Persisted to backing store = " + Boolean.valueOf(repository.updateCheck(config.getName(), config.getNodes())));
			}

		};
	}

	private MongoCollection getCollection() {
		if(checks == null) {
			checks = getChecksCollection();
		}
		return checks;
	}

}
