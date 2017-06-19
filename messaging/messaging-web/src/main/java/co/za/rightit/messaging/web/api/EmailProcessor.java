package co.za.rightit.messaging.web.api;

import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import co.za.rightit.commons.event.EventService;
import co.za.rightit.commons.event.EventSubscriber;
import co.za.rightit.messaging.email.EmailAccount;
import co.za.rightit.messaging.email.EmailAccountRepository;
import co.za.rightit.messaging.email.EmailEvent;
import co.za.rightit.messaging.email.template.TemplateServiceRepository;
import co.za.rightit.messaging.web.model.EmailData;
import co.za.rightit.messaging.web.model.EmailRequestEvent;

public abstract class EmailProcessor<E extends EmailRequestEvent> implements EventSubscriber {
	protected final EventService eventService;
	protected final EmailAccountRepository repository;
	protected final TemplateServiceRepository templateServiceRepository;
	
	@Inject
	public EmailProcessor(EventService eventService, EmailAccountRepository repository, TemplateServiceRepository templateServiceRepository) {
		this.eventService = Preconditions.checkNotNull(eventService, "eventService");
		this.repository = Preconditions.checkNotNull(repository, "repository");
		this.templateServiceRepository = Preconditions.checkNotNull(templateServiceRepository, "templateServiceRepository");
	}
	
	protected void onEmailRequestEvent(E evt) {
		Optional<EmailAccount> optional = repository.findEmailAccount(evt.getDomain());
		if(optional.isPresent()) {
			eventService.post(createEmailEvent(evt.getData(), optional.get()));
		} else {
			throw new IllegalStateException(String.format("Email account with domain=%s not found", evt.getDomain()));
		}
	}
	
	public abstract EmailEvent createEmailEvent(EmailData request, EmailAccount emailAccount);
	
}
