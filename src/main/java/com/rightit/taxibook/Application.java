package com.rightit.taxibook;

import javax.inject.Singleton;
import javax.validation.Validator;

import org.apache.commons.configuration.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.mongodb.client.MongoDatabase;
import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.provider.ConfigurationProvider;
import com.rightit.taxibook.provider.MongoProvider;
import com.rightit.taxibook.provider.ObjectMapperProvider;
import com.rightit.taxibook.provider.TemplateMergerProvider;
import com.rightit.taxibook.provider.ValidatorProvider;
import com.rightit.taxibook.repository.Repository;
import com.rightit.taxibook.repository.UseRepository;
import com.rightit.taxibook.repository.VerificationTokenRepository;
import com.rightit.taxibook.service.mail.EmailService;
import com.rightit.taxibook.service.mail.EmailServiceImpl;
import com.rightit.taxibook.service.password.DefaultPasswordHashService;
import com.rightit.taxibook.service.password.PasswordHashService;
import com.rightit.taxibook.service.token.VerificationTokenService;
import com.rightit.taxibook.service.token.VerificationTokenServiceImpl;
import com.rightit.taxibook.service.user.UserService;
import com.rightit.taxibook.service.user.UserServiceImpl;
import com.rightit.taxibook.template.TemplateMerger;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class Application extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				bind(new TypeLiteral<Repository<User>>() {}).to(UseRepository.class);
				bind(new TypeLiteral<Repository<VerificationToken>>() {}).to(VerificationTokenRepository.class);
				bind(UserService.class).to(UserServiceImpl.class);
				bind(VerificationTokenService.class).to(VerificationTokenServiceImpl.class);
				bind(PasswordHashService.class).to(DefaultPasswordHashService.class);
				bind(EmailService.class).to(EmailServiceImpl.class);
				bind(TemplateMerger.class).toProvider(TemplateMergerProvider.class).in(Singleton.class);
				bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
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
