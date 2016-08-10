package co.za.rightit.taxibook.module;

import javax.inject.Singleton;
import javax.validation.Validator;

import com.google.inject.AbstractModule;

import co.za.rightit.commons.provider.ValidatorProvider;
import co.za.rightit.taxibook.provider.TemplateMergerProvider;
import co.za.rightit.taxibook.service.authentication.JWTTokenService;
import co.za.rightit.taxibook.service.authentication.LoginService;
import co.za.rightit.taxibook.service.authentication.LoginServiceImpl;
import co.za.rightit.taxibook.service.authentication.TokenAuthenticationService;
import co.za.rightit.taxibook.service.mail.EmailService;
import co.za.rightit.taxibook.service.mail.EmailServiceImpl;
import co.za.rightit.taxibook.service.password.DefaultPasswordHashService;
import co.za.rightit.taxibook.service.password.PasswordHashService;
import co.za.rightit.taxibook.service.user.UserService;
import co.za.rightit.taxibook.service.user.UserServiceImpl;
import co.za.rightit.taxibook.service.verify.TokenGenerator;
import co.za.rightit.taxibook.service.verify.TokenGeneratorImpl;
import co.za.rightit.taxibook.service.verify.VerificationTokenService;
import co.za.rightit.taxibook.service.verify.VerificationTokenServiceImpl;
import co.za.rightit.taxibook.template.TemplateMerger;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserService.class).to(UserServiceImpl.class);
		bind(VerificationTokenService.class).to(VerificationTokenServiceImpl.class);
		bind(PasswordHashService.class).to(DefaultPasswordHashService.class);
		bind(EmailService.class).to(EmailServiceImpl.class);
		bind(TokenAuthenticationService.class).to(JWTTokenService.class);
		bind(LoginService.class).to(LoginServiceImpl.class);
		bind(TokenGenerator.class).to(TokenGeneratorImpl.class);
		
		bind(TemplateMerger.class).toProvider(TemplateMergerProvider.class).in(Singleton.class);
		bind(Validator.class).toProvider(ValidatorProvider.class).asEagerSingleton();
	}
}
