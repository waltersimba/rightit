package co.za.rightit.healthchecks.api.modules;

import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;

import co.za.rightit.healthchecks.mongo.CheckRepository;
import co.za.rightit.healthchecks.mongo.CheckRepositoryImpl;

public class ChecksModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	public CheckRepository checkRepository(MongoClient client) {
		Jongo jongo = new Jongo(client.getDB("config"), new JacksonMapper.Builder()
				.registerModule(new JodaModule())
				.enable(MapperFeature.AUTO_DETECT_GETTERS)
				.build());
		return new CheckRepositoryImpl(jongo.getCollection("checks"));
	}
}
