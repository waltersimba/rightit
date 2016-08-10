package co.za.rightit.taxibook.service.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.za.rightit.commons.exceptions.ApplicationRuntimeException;
import co.za.rightit.taxibook.domain.User;
import co.za.rightit.taxibook.domain.VerificationToken;
import co.za.rightit.taxibook.service.mail.EmailMessage;
import co.za.rightit.taxibook.service.mail.EmailMessage.EmailContentType;
import co.za.rightit.taxibook.service.mail.EmailMessage.EmailMessageBuilder;
import co.za.rightit.taxibook.template.MergeException;
import co.za.rightit.taxibook.template.TemplateMerger;

public class ResetPasswordMessageBuilder implements Function<User, EmailMessage> {
	private Logger logger = LoggerFactory.getLogger(ResetPasswordMessageBuilder.class);
	private final Optional<VerificationToken> optionalToken;
	private final TemplateMerger templateMerger;

	public ResetPasswordMessageBuilder(Optional<VerificationToken> optionalToken, TemplateMerger templateMerger) {
		this.optionalToken = optionalToken;
		this.templateMerger = templateMerger;
	}

	@Override
	public EmailMessage apply(User user) {
		try {
			final VerificationToken verificationToken = optionalToken.get();
			final Map<String, String> templateValues = buildTemplateMap(user.getFirstName(), verificationToken.getToken());
			final String htmlMessage = templateMerger.mergeTemplateIntoString("ResetPassword", templateValues);
			return buildEmailMessage(user, htmlMessage);
		} catch (MergeException ex) {
			String errorMessage = String.format("Failed to build reset password email for %s: %s", user.getEmailAddress(), ex.getMessage()); 
			logger.error(errorMessage);
			throw new ApplicationRuntimeException(errorMessage);
		}		
	}
	
	private Map<String, String> buildTemplateMap(String firstName, String token) {
		final Map<String, String> templateMap = new HashMap<>();
		templateMap.put("firstName", firstName);
		templateMap.put("resetPasswordUrl", "www.taxibook.co.za/resetPassword/" + token);
		templateMap.put("helpEmailAddress", "support@rightit.co.za");
		return templateMap;
	}

	private EmailMessage buildEmailMessage(User user, String htmlMessage) {
		return new EmailMessageBuilder()
				.withSenderName("Taxibook")
				.withSenderEmail("no-reply@rightit.co.za")
				.withSubject("Reset Password For Taxibook Account")
				.withRecipient(user.getEmailAddress())
				.withMessage(htmlMessage)
				.withContentType(EmailContentType.HTML)
				.build();
	}
}
