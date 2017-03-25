package co.za.rightit.messaging.web.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import co.za.rightit.messaging.email.CachingFileEmailAccountRepository;
import co.za.rightit.messaging.email.EmailAccountRepository;
import co.za.rightit.messaging.email.FileEmailAccountRepository;
import co.za.rightit.messaging.web.api.EmailRequestProcessor;
import co.za.rightit.messaging.web.model.ApplicationOptions;

public class MessagingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EmailRequestProcessor.class).asEagerSingleton();
		bind(String.class).annotatedWith(Names.named("email-account-cache-spec")).toInstance("expireAfterAccess=30d");
	}
	
	@Singleton
	@Provides
	public EmailAccountRepository emailAccountRepository(ApplicationOptions options, @Named("email-account-cache-spec") String cacheSpec) {
		return new CachingFileEmailAccountRepository(new FileEmailAccountRepository(options.getEmailAccountsFile()), cacheSpec);
	}

}
