package co.za.rightit.healthchecks.mongo.modules;

import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;

public class MongoDBModule extends AbstractModule {

    @Override
    protected void configure() {
        
    }
    
    @Provides
    @Singleton
    public MongoClient mongoClient() {
    	return new MongoClient();
    }
    
    @Provides
    @Singleton
    public Jongo jongo(MongoClient client) {
    	return new Jongo(client.getDB("config"), new JacksonMapper.Builder()
				.registerModule(new JodaModule())
				.enable(MapperFeature.AUTO_DETECT_GETTERS)
				.build());
    }
    
}
