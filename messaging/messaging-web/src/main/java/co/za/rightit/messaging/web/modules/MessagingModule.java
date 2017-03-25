package co.za.rightit.messaging.web.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import co.za.rightit.messaging.email.EmailAccountRepository;
import co.za.rightit.messaging.email.FileEmailAccountRepository;
import co.za.rightit.messaging.web.api.EmailRequestProcessor;
import co.za.rightit.messaging.web.model.ApplicationOptions;

public class MessagingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EmailRequestProcessor.class).asEagerSingleton();
	}
	
	@Singleton
	@Provides
	public EmailAccountRepository emailAccountRepository(ApplicationOptions options) {
		return new FileEmailAccountRepository(options.getEmailAccountsFile());
	}

}
