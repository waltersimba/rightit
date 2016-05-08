package com.rightit.taxibook.service.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.service.mail.EmailMessage;
import com.rightit.taxibook.service.mail.EmailMessage.EmailContentType;
import com.rightit.taxibook.service.mail.EmailMessage.EmailMessageBuilder;
import com.rightit.taxibook.template.MergeException;
import com.rightit.taxibook.template.TemplateMerger;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;

public class ResetPasswordConfirmationEmailBuilder implements Function<User, EmailMessage> {
	
	private Logger logger = LoggerFactory.getLogger(ResetPasswordConfirmationEmailBuilder.class);
	private final TemplateMerger templateMerger;
	
	public ResetPasswordConfirmationEmailBuilder(TemplateMerger templateMerger) {
		this.templateMerger = templateMerger;
	}
	
	@Override
	public EmailMessage apply(User user) {
		try {
			final Map<String, String> templateValues = buildTemplateMap(user.getFirstName());
			final String htmlMessage = templateMerger.mergeTemplateIntoString("ResetPasswordConfirmation", templateValues);
			return buildEmailMessage(user, htmlMessage);
		} catch (MergeException ex) {
			String errorMessage = String.format("Failed to build reset password confirmation email for %s: %s", user.getEmailAddress(), ex.getMessage()); 
			logger.error(errorMessage);
			throw new ApplicationRuntimeException(errorMessage);
		}				
	}	
	
	private Map<String, String> buildTemplateMap(String firstName) {
		final Map<String, String> templateMap = new HashMap<>();
		templateMap.put("firstName", firstName);
		templateMap.put("supportEmailAddress", "support@rightit.co.za");
		return templateMap;
	}

	private EmailMessage buildEmailMessage(User user, String htmlMessage) {
		return new EmailMessageBuilder()
				.withSenderName("Taxibook")
				.withSenderEmail("no-reply@rightit.co.za")
				.withSubject("Taxibook Password Change Confirmation")
				.withRecipient(user.getEmailAddress())
				.withMessage(htmlMessage)
				.withContentType(EmailContentType.HTML)
				.build();
	}
}