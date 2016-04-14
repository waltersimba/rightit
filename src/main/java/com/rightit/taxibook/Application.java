package com.rightit.taxibook;

import javax.validation.Validator;

import org.apache.commons.configuration.Configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.mongodb.client.MongoDatabase;
import com.rightit.taxibook.provider.ConfigurationProvider;
import com.rightit.taxibook.provider.MongoProvider;
import com.rightit.taxibook.provider.ValidatorProvider;
import com.rightit.taxibook.repository.UseRepositoryImpl;
import com.rightit.taxibook.repository.UserRepository;
import com.rightit.taxibook.service.UserService;
import com.rightit.taxibook.service.UserServiceImpl;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class Application extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				bind(UserRepository.class).to(UseRepositoryImpl.class);
				bind(UserService.class).to(UserServiceImpl.class);
				bind(Validator.class).toProvider(ValidatorProvider.class).asEagerSingleton();
				bind(Configuration.class).toProvider(ConfigurationProvider.class).asEagerSingleton();
				bind(MongoDatabase.class).toProvider(MongoProvider.class).asEagerSingleton();
				
				ResourceConfig rc = new PackagesResourceConfig("com.rightit.taxibook");
				for (Class<?> resource : rc.getClasses()) {
					bind(resource);
				}
				
				serve( "/api/*" ).with( GuiceContainer.class );
			}
		});
	}

}
