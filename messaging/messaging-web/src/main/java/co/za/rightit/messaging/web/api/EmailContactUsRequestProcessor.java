package co.za.rightit.messaging.web.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import co.za.rightit.commons.event.EventService;
import co.za.rightit.commons.event.EventSubscriber;
import co.za.rightit.messaging.email.EmailAccount;
import co.za.rightit.messaging.email.EmailAccountRepository;
import co.za.rightit.messaging.email.EmailEvent;
import co.za.rightit.messaging.email.EmailMessage;
import co.za.rightit.messaging.email.EmailMessage.EmailContentType;
import co.za.rightit.messaging.email.template.TemplateService;
import co.za.rightit.messaging.email.template.TemplateServiceRepository;
import co.za.rightit.messaging.web.model.EmailContactUsRequest;
import co.za.rightit.messaging.web.model.EmailContactUsRequestEvent;

@Singleton
public class EmailContactUsRequestProcessor implements EventSubscriber {
	
	private static final String TEMPLATE_NAME = "contact-us-request";
	private static final String SUBJECT = "Contact inquiry";
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailContactUsRequestProcessor.class);
	private final EventService eventService;
	private final EmailAccountRepository repository;
	private final TemplateServiceRepository templateServiceRepository;
	
	@Inject
	public EmailContactUsRequestProcessor(EventService eventService, EmailAccountRepository repository, TemplateServiceRepository templateServiceRepository) {
		this.eventService = Preconditions.checkNotNull(eventService, "eventService");
		this.repository = Preconditions.checkNotNull(repository, "repository");
		this.templateServiceRepository = Preconditions.checkNotNull(templateServiceRepository, "templateServiceRepository");
	}
	
	@Subscribe
	public void onEmailRequestEvent(EmailContactUsRequestEvent evt) {
		Optional<EmailAccount> optional = repository.findEmailAccount(evt.getDomainName());
		if(optional.isPresent()) {
			eventService.post(createEmailEvent(evt.getRequest(), optional.get()));
		} else {
			throw new IllegalStateException(String.format("Email account with domain=%s not found", evt.getDomainName()));
		}
	}

	private EmailEvent createEmailEvent(EmailContactUsRequest request, EmailAccount account) {
		LOGGER.info("build email message for {}", request.getTo());
		EmailMessage emailMessage = new EmailMessage.EmailMessageBuilder()
				.withContentType(EmailContentType.HTML)
				.withMessage(generateContent(request, account.getDomain()))
				.withRecipient(account.getTo())
				.withSenderEmail(account.getFrom())
				.withSubject(SUBJECT)
				.withReplyTo(request.getTo())
				.withEmailServerSettings(account.getSettings())
				.build();
		return new EmailEvent(emailMessage);
	}
	
	private String generateContent(EmailContactUsRequest request, String domain) {
		final TemplateService templateService = templateServiceRepository.getTemplateService(domain);
		return templateService.generateContent(TEMPLATE_NAME, getTemplateVariables(request));
	}
	
	@SuppressWarnings("serial")
	private Map<String, Object> getTemplateVariables(EmailContactUsRequest request) {
		return Collections.unmodifiableMap(new HashMap<String, String>() {
			{
				put("email_address", request.getTo());
				put("phone_number", request.getPhoneNumber());
				put("message", request.getMessage());
			}
		});
	}

	@Override
	public String getSubscriberId() {
		return getClass().getName();
	}
	
}
