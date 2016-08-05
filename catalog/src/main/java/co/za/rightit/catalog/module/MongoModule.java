package co.za.rightit.catalog.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoModule extends AbstractModule {

	@Override
	protected void configure() {
		//ignore		
	}
	
	@SuppressWarnings("resource")
	@Provides
	@Singleton
	public MongoDatabase mongoDatabase(
			@Named("mongo.host") String host, 
			@Named("mongo.port") int port, 
			@Named("mongo.database") String databaseName) {
		return new MongoClient(host, port).getDatabase(databaseName);
	}

}
