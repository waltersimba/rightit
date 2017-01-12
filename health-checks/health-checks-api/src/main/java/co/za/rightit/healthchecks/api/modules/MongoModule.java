package co.za.rightit.healthchecks.api.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;

public class MongoModule extends AbstractModule {

    @Override
    protected void configure() {
        
    }
    
    @Provides
    @Singleton
    public MongoClient mongoClient() {
    	return new MongoClient();
    }
    
}
