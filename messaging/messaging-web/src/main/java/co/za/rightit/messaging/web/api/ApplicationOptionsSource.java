package co.za.rightit.messaging.web.api;

import java.nio.file.Paths;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.za.rightit.messaging.web.model.ApplicationOptions;

public class ApplicationOptionsSource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationOptionsSource.class);
	private static final String EMAIL_SETTINGS_FILE = "email-settings-file";
	private final ServletContext servletContext;
	
	public ApplicationOptionsSource(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public ApplicationOptions getOptions() {
		ApplicationOptions.Builder builder = ApplicationOptions.newBuilder();
		if(getEmailSettingsFile() != null) {
			builder.withEmailAccountsFile(Paths.get(getEmailSettingsFile()));
		}
		ApplicationOptions options = builder.build();
		LOGGER.info("email-settings-file={}", options.getEmailAccountsFile().toUri());
		return options;
	}
	
	private String getEmailSettingsFile() {
		return servletContext.getInitParameter(EMAIL_SETTINGS_FILE);
	}
	
}
