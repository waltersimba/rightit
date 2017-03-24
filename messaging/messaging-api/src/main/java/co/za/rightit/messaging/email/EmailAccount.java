package co.za.rightit.messaging.email;

public class EmailAccount {
	
	private String domain;
	
	private EmailServerSettings settings;
	
	public EmailAccount(String domain, EmailServerSettings settings) {
		this.domain = domain;
		this.settings = settings;
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
	
}
