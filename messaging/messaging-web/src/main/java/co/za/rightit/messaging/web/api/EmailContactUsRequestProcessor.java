package co.za.rightit.messaging.web.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import co.za.rightit.commons.event.EventService;
import co.za.rightit.messaging.email.EmailAccount;
import co.za.rightit.messaging.email.EmailAccountRepository;
import co.za.rightit.messaging.email.EmailEvent;
import co.za.rightit.messaging.email.EmailMessage;
import co.za.rightit.messaging.email.EmailMessage.EmailContentType;
import co.za.rightit.messaging.email.template.TemplateService;
import co.za.rightit.messaging.email.template.TemplateServiceRepository;
import co.za.rightit.messaging.web.model.EmailContactUsReply;
import co.za.rightit.messaging.web.model.EmailContactUsReplyEvent;
import co.za.rightit.messaging.web.model.EmailContactUsRequest;
import co.za.rightit.messaging.web.model.EmailContactUsRequestEvent;
import co.za.rightit.messaging.web.model.EmailData;

@Singleton
public class EmailContactUsRequestProcessor extends EmailProcessor<EmailContactUsRequestEvent> {

	private static final String TEMPLATE_NAME = "contact-us-request";
	private static final String SUBJECT = "Contact inquiry";
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailContactUsRequestProcessor.class);

	@Inject
	public EmailContactUsRequestProcessor(EventService eventService, EmailAccountRepository repository,
			TemplateServiceRepository templateServiceRepository) {
		super(eventService, repository, templateServiceRepository);
	}

	@Subscribe
	public void onEmailContactUsRequestEvent(EmailContactUsRequestEvent evt) {
		try {
			onEmailRequestEvent(evt);
		} finally {
			EmailContactUsReply reply = new EmailContactUsReply()
					.withContactName(evt.getData().getContactName())
					.withTo(evt.getData().getTo());
			eventService.post(new EmailContactUsReplyEvent(evt.getDomain(), reply));
		}
	}

	@Override
	public EmailEvent createEmailEvent(EmailData emailData, EmailAccount account) {
		EmailContactUsRequest request = (EmailContactUsRequest) emailData;
		LOGGER.info("build email message for {}", request.getTo());
		EmailMessage emailMessage = new EmailMessage.EmailMessageBuilder().withContentType(EmailContentType.HTML)
				.withMessage(generateContent(request, account.getDomain())).withRecipient(account.getTo())
				.withSenderEmail(account.getFrom()).withSubject(SUBJECT).withReplyTo(request.getTo())
				.withEmailServerSettings(account.getSettings()).build();
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
