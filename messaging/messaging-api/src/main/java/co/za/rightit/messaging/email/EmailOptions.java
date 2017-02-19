package co.za.rightit.messaging.email;

public class EmailOptions {
	
	private final int smtpPort;
	private final String hostName;
	private final String username;
	private final String password;
	private final Boolean startTLSEnabled;
	
	public EmailOptions(Builder builder) {
		smtpPort = builder.smtpPort;
		hostName = builder.hostName;
		username = builder.username;
		password = builder.password;
		startTLSEnabled = builder.startTLSEnabled;
	}
	
	public int getSmtpPort() {
		return smtpPort;
	}

	public String getHostName() {
		return hostName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Boolean isStartTLSEnabled() {
		return startTLSEnabled;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		private int smtpPort;
		private String hostName;
		private String username;
		private String password;
		private Boolean startTLSEnabled;
		
		public Builder withSmtpPort(int smtpPort) {
			this.smtpPort = smtpPort;
			return this;
		}
		
		public Builder withHostName(String hostName) {
			this.hostName = hostName;
			return this;
		}
		
		public Builder withUsername(String username) {
			this.username = username;
			return this;
		}
		
		public Builder withPassword(String password) {
			this.password = password;
			return this;
		}
		
		public Builder withStartTLSEnabled(Boolean startTLSEnabled) {
			this.startTLSEnabled = startTLSEnabled;
			return this;
		}
		
		public EmailOptions build() {
			return new EmailOptions(this);
		}
	}
}
