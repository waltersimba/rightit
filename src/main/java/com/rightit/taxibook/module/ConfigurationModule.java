package com.rightit.taxibook.module;

import javax.inject.Singleton;

import org.apache.commons.configuration.Configuration;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mongodb.client.MongoDatabase;
import com.rightit.taxibook.provider.ConfigurationProvider;
import com.rightit.taxibook.provider.MongoProvider;
import com.rightit.taxibook.provider.ObjectMapperProvider;

public class ConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
		bind(Configuration.class).toProvider(ConfigurationProvider.class).asEagerSingleton();
		bind(MongoDatabase.class).toProvider(MongoProvider.class).asEagerSingleton();
		bind(JacksonFeature.class);
		
		bindConstant().annotatedWith(Names.named("applicationName")).to("taxibook");
	}

}
