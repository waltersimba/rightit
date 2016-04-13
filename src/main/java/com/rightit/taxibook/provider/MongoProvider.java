package com.rightit.taxibook.provider;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;

import com.google.inject.Provider;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoProvider implements Provider<MongoDatabase> {

	private Provider<Configuration> configurationProvider;
	private MongoClient mongoClient = null;

	@Inject
	public MongoProvider(Provider<Configuration>configurationProvider) {
		this.configurationProvider = configurationProvider;
	}

	@Override
	public MongoDatabase get() {
		return getMongoClient().getDatabase(getConfiguration().getString("mongo.databaseName"));
	}

	private MongoClient getMongoClient() {
		if (null == mongoClient) {
			final String host = getConfiguration().getString("mongo.host");
			final int port = getConfiguration().getInt("mongo.port");
			mongoClient = new MongoClient(host, port);
		}
		return mongoClient;
	}
	
	private Configuration getConfiguration() {
		return configurationProvider.get();
	}
}
