package co.za.rightit.messaging.email;

import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import co.za.rightit.messaging.email.EmailMessage.EmailContentType;

@Singleton
public class EmailServiceImpl implements EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Override
	public void send(EmailMessage emailMessage) throws EmailException {
		final Optional<Email> optionalEmail = new EmailMessageBuilder(emailMessage.getMessage()).apply(emailMessage.getContentType());
		if(!optionalEmail.isPresent()) {
			throw new EmailException("Failed to create an instance of an Email object");
		}
		final Email email = optionalEmail.get();
		final EmailServerSettings settings = emailMessage.getSettings();
		email.setSmtpPort(settings.getSmtpPort());
		email.setHostName(settings.getHostName());
		email.setAuthentication(settings.getUsername(), settings.getPassword());
		email.setStartTLSEnabled(settings.isStartTLSEnabled());
		email.setFrom(emailMessage.getSenderEmail(), emailMessage.getSenderName());
		email.setSubject(emailMessage.getSubject());
		for(String recipient : emailMessage.getRecipients()) {
			email.addTo(recipient);
		}
		Optional<String> replyTo = emailMessage.getReplyTo();
		if(replyTo.isPresent()) {
			email.addReplyTo(replyTo.get());
		}
		email.send();
		LOGGER.info("sent email to \"{}\"", emailMessage.getRecipients());
	}

	protected class EmailMessageBuilder implements Function<EmailContentType, Optional<Email>> {

		private final String emailMessage;

		public EmailMessageBuilder(String emailMessage) {
			this.emailMessage = emailMessage;
		}

		@Override
		public Optional<Email> apply(EmailContentType contentType) {
			Optional<Email> email = null;
			try {
				if(EmailContentType.TEXT.equals(contentType)) {
					email = Optional.of(new SimpleEmail().setMsg(emailMessage));
				}
				else if(EmailContentType.HTML.equals(contentType)) {
					email = Optional.of(new HtmlEmail().setHtmlMsg(emailMessage));
				}
			} catch(EmailException ex) {
				LOGGER.error("Failed to create instance of an Email object : " + ex.getMessage());
				email = Optional.empty();
			}
			return email;
		}	
	}

}
