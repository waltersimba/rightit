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
import co.za.rightit.messaging.web.model.ContactRequest;
import co.za.rightit.messaging.web.model.EmailRequestEvent;

@Singleton
public class EmailRequestProcessor implements EventSubscriber {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRequestProcessor.class);
	private final EventService eventService;
	private final EmailAccountRepository repository;
	private final TemplateServiceRepository templateServiceRepository;
	
	@Inject
	public EmailRequestProcessor(EventService eventService, EmailAccountRepository repository, TemplateServiceRepository templateServiceRepository) {
		this.eventService = Preconditions.checkNotNull(eventService, "eventService");
		this.repository = Preconditions.checkNotNull(repository, "repository");
		this.templateServiceRepository = Preconditions.checkNotNull(templateServiceRepository, "templateServiceRepository");
	}
	
	@Subscribe
	public void onEmailRequestEvent(EmailRequestEvent evt) {
		Optional<EmailAccount> optional = repository.findEmailAccount(evt.getDomainName());
		if(optional.isPresent()) {
			eventService.post(createEmailEvent(evt.getEmailRequest(), optional.get()));
		} else {
			throw new IllegalStateException(String.format("Email account with domain=%s not found", evt.getDomainName()));
		}
	}

	private EmailEvent createEmailEvent(ContactRequest request, EmailAccount account) {
		LOGGER.info("build email message for {}", request.getTo());
		EmailMessage emailMessage = new EmailMessage.EmailMessageBuilder()
				.withContentType(EmailContentType.HTML)
				.withMessage(generateContent(request, account.getDomain()))
				.withRecipient(account.getTo())
				.withSenderEmail(account.getFrom())
				.withSubject("Contact inquiry")
				.withReplyTo(request.getTo())
				.withEmailServerSettings(account.getSettings())
				.build();
		return new EmailEvent(emailMessage);
	}
	
	private String generateContent(ContactRequest request, String domain) {
		final TemplateService templateService = templateServiceRepository.getTemplateService(domain);
		return templateService.generateContent("contact-us-request", getTemplateVariables(request));
	}
	
	@SuppressWarnings("serial")
	private Map<String, Object> getTemplateVariables(ContactRequest request) {
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
