package co.za.rightit.messaging.web.modules;

import com.google.inject.AbstractModule;

import co.za.rightit.messaging.email.EmailAccountRepository;
import co.za.rightit.messaging.email.FileEmailAccountRepository;
import co.za.rightit.messaging.web.api.EmailRequestProcessor;

public class MessagingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EmailAccountRepository.class).to(FileEmailAccountRepository.class);
		bind(EmailRequestProcessor.class).asEagerSingleton();
	}

}
