package com.rightit.taxibook.service.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.service.mail.EmailMessage;
import com.rightit.taxibook.service.mail.EmailMessage.EmailContentType;
import com.rightit.taxibook.service.mail.EmailMessage.EmailMessageBuilder;
import com.rightit.taxibook.template.MergeException;
import com.rightit.taxibook.template.TemplateMerger;
import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;

public class VerifyEmailMessageBuilder implements Function<User, EmailMessage> {
	
	private Logger logger = LoggerFactory.getLogger(VerifyEmailMessageBuilder.class);
	private final Optional<VerificationToken> optionalToken;
	private final TemplateMerger templateMerger;
	
	public VerifyEmailMessageBuilder(Optional<VerificationToken> optionalToken, TemplateMerger templateMerger) {
		this.optionalToken = optionalToken;
		this.templateMerger = templateMerger;
	}
	
	@Override
	public EmailMessage apply(User user) {
		try {
			final VerificationToken verificationToken = optionalToken.get();
			final Map<String, String> templateValues = buildTemplateMap(user.getFirstName(), verificationToken.getToken());
			final String htmlMessage = templateMerger.mergeTemplateIntoString("VerifyMail", templateValues);
			return buildEmailMessage(user, htmlMessage);
		} catch (MergeException ex) {
			String errorMessage = String.format("Failed to build token verification email for %s: %s", user.getEmailAddress(), ex.getMessage()); 
			logger.error(errorMessage);
			throw new ApplicationRuntimeException(errorMessage);
		}				
	}	
	
	private Map<String, String> buildTemplateMap(String firstName, String token) {
		final Map<String, String> templateMap = new HashMap<>();
		templateMap.put("firstName", firstName);
		templateMap.put("verificationUrl", "www.taxibook.co.za/verify/" + token);
		templateMap.put("daysBeforeExpiry", Integer.toString(5));
		templateMap.put("generateEmailTokenUrl", "www.taxibook.co.za/verify/tokens/" + token);
		templateMap.put("helpEmailAddress", "support@rightit.co.za");
		return templateMap;
	}

	private EmailMessage buildEmailMessage(User user, String htmlMessage) {
		return new EmailMessageBuilder()
				.withSenderName("Taxibook")
				.withSenderEmail("no-reply@rightit.co.za")
				.withSubject("Your Taxibook account - Verify your E-mail address")
				.withRecipient(user.getEmailAddress())
				.withMessage(htmlMessage)
				.withContentType(EmailContentType.HTML)
				.build();
	}
}