package com.rightit.taxibook.service.mail;

import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import com.rightit.taxibook.service.mail.EmailMessage.EmailContentType;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;

public class EmailServiceImpl implements EmailService {

	private Logger logger = Logger.getLogger(EmailServiceImpl.class);
	
	@Override
	public void send(EmailMessage emailMessage) {
		try {
			final Optional<Email> optionalEmail = new EmailMessageBuilder(emailMessage.getMessage()).apply(emailMessage.getContentType());
			
			if(!optionalEmail.isPresent()) {
				throw new ApplicationRuntimeException("Failed to create an instance of an Email object");
			}
			
			final Email email = optionalEmail.get();
			
			email.setSmtpPort(587);
			email.setHostName("smtp.zoho.com");
			email.setAuthentication("app@rightit.co.za", "pr0f1l3!");
			email.setStartTLSEnabled(true);
			email.setFrom(emailMessage.getSenderEmail(), emailMessage.getSenderName());
			email.setSubject(emailMessage.getSubject());
			for(String recipient : emailMessage.getRecipients()) {
				email.addTo(recipient);
			}
			
			String messageId = email.send();
			logger.info(String.format("Email with message id %s sent!", messageId));
		} catch (EmailException ex) {
			logger.error(String.format("Failed to send email to %s", emailMessage.getRecipients()));
			throw new ApplicationRuntimeException("Failed to send email: " + ex.getMessage());
		}
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
				logger.error("Failed to create instance of an Email object : " + ex.getMessage());
				email = Optional.empty();
			}
			return email;
		}	
	}

}
