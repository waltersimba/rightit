package co.za.rightit.messaging.email;

import java.nio.file.Path;

import com.google.common.base.MoreObjects;

public class EmailAccount {
	
	private String domain;
	private String from;
	private String to;
	private EmailServerSettings settings;
	private String templatePath;
	
	public EmailAccount() {}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public EmailServerSettings getSettings() {
		return settings;
	}

	public void setSettings(EmailServerSettings settings) {
		this.settings = settings;
	}
	
	public String getTemplatePath() {
		return templatePath;
	}
	
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("domain", domain)
				.add("from", from)
				.add("to", to)
				.add("settings", settings)
				.add("template_path", templatePath)
				.toString();
	}
	
}
