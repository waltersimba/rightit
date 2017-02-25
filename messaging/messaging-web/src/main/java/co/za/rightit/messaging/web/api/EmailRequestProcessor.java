package co.za.rightit.messaging.web.api;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import co.za.rightit.commons.event.EventService;
import co.za.rightit.commons.event.EventSubscriber;
import co.za.rightit.messaging.email.EmailEvent;
import co.za.rightit.messaging.email.EmailOptions;
import co.za.rightit.messaging.web.model.EmailRequest;
import co.za.rightit.messaging.web.model.EmailRequestEvent;

@Singleton
public class EmailRequestProcessor implements EventSubscriber {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRequestProcessor.class);
	private final EventService eventService;
	private final EmailOptionsRepository repository;
	private final Provider<String> usernameProvider;
	
	@Inject
	public EmailRequestProcessor(EventService eventService, EmailOptionsRepository repository, Provider<String> usernameProvider) {
		this.eventService = Preconditions.checkNotNull(eventService, "eventService");
		this.repository = Preconditions.checkNotNull(repository, "repository");
		this.usernameProvider = Preconditions.checkNotNull(usernameProvider, "usernameProvider");
	}
	
	@Subscribe
	public void onEmailRequestEvent(EmailRequestEvent evt) {
		String username = usernameProvider.get();
		Optional<EmailOptions> optinal = repository.findEmailOptions(username);
		if(optinal.isPresent()) {
			eventService.post(createEmailEvent(evt.getEmailRequest(), optinal.get()));
		} else {
			LOGGER.error("Email configuration not found for user: {}", username);
			throw new RuntimeException(String.format("Email configuration not found for user: %s", username));
		}
	}

	private EmailEvent createEmailEvent(EmailRequest request, EmailOptions options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubscriberId() {
		return getClass().getName();
	}
	
}
