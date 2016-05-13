package com.rightit.taxibook.module;

import javax.inject.Singleton;
import javax.validation.Validator;

import com.google.inject.AbstractModule;
import com.rightit.taxibook.provider.TemplateMergerProvider;
import com.rightit.taxibook.provider.ValidatorProvider;
import com.rightit.taxibook.service.authentication.JWTTokenService;
import com.rightit.taxibook.service.authentication.TokenAuthenticationService;
import com.rightit.taxibook.service.mail.EmailService;
import com.rightit.taxibook.service.mail.EmailServiceImpl;
import com.rightit.taxibook.service.password.DefaultPasswordHashService;
import com.rightit.taxibook.service.password.PasswordHashService;
import com.rightit.taxibook.service.user.UserService;
import com.rightit.taxibook.service.user.UserServiceImpl;
import com.rightit.taxibook.service.verify.VerificationTokenService;
import com.rightit.taxibook.service.verify.VerificationTokenServiceImpl;
import com.rightit.taxibook.template.TemplateMerger;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserService.class).to(UserServiceImpl.class);
		bind(VerificationTokenService.class).to(VerificationTokenServiceImpl.class);
		bind(PasswordHashService.class).to(DefaultPasswordHashService.class);
		bind(EmailService.class).to(EmailServiceImpl.class);
		bind(TokenAuthenticationService.class).to(JWTTokenService.class);
		
		bind(TemplateMerger.class).toProvider(TemplateMergerProvider.class).in(Singleton.class);
		bind(Validator.class).toProvider(ValidatorProvider.class).asEagerSingleton();
	}
}
