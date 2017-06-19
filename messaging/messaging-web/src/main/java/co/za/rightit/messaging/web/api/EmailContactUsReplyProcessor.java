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
import co.za.rightit.messaging.web.model.EmailData;

@Singleton
public class EmailContactUsReplyProcessor extends EmailProcessor<EmailContactUsReplyEvent> {
	
	private static final String TEMPLATE_NAME = "contact-us-reply";
	private static final String SUBJECT = "Request received";
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailContactUsReplyProcessor.class);
	
	@Inject
	public EmailContactUsReplyProcessor(EventService eventService, EmailAccountRepository repository, TemplateServiceRepository templateServiceRepository) {
		super(eventService, repository, templateServiceRepository);
	}

	@Subscribe
	public void onEmailContactUsRequestEvent(EmailContactUsReplyEvent evt) {
		onEmailRequestEvent(evt);
	}
	
	@Override
	public EmailEvent createEmailEvent(EmailData data, EmailAccount account) {
		EmailContactUsReply reply = (EmailContactUsReply) data;
		LOGGER.info("build email message for {}", reply.getTo());
		EmailMessage emailMessage = new EmailMessage.EmailMessageBuilder()
				.withContentType(EmailContentType.HTML)
				.withMessage(generateContent(reply, account.getDomain()))
				.withRecipient(reply.getTo())
				.withSenderEmail(account.getFrom())
				.withSubject(SUBJECT)
				.withEmailServerSettings(account.getSettings())
				.build();
		return new EmailEvent(emailMessage);
	}
		
	private String generateContent(EmailContactUsReply reply, String domain) {
		final TemplateService templateService = templateServiceRepository.getTemplateService(domain);
		return templateService.generateContent(TEMPLATE_NAME, getTemplateVariables(reply));
	}
	
	@SuppressWarnings("serial")
	private Map<String, Object> getTemplateVariables(EmailContactUsReply reply) {
		return Collections.unmodifiableMap(new HashMap<String, String>() {
			{
				put("name", reply.getContactName());
			}
		});
	}

	@Override
	public String getSubscriberId() {
		return getClass().getName();
	}
	
}
