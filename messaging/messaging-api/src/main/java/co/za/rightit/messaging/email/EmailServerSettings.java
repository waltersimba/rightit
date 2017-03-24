package co.za.rightit.messaging.email;

import com.google.gson.annotations.SerializedName;

public class EmailServerSettings {
	
	@SerializedName("smpt_port")
	private final int smtpPort;
	@SerializedName("host_name")
	private final String hostName;
	private final String username;
	private final String password;
	@SerializedName("start_tls_enabled")
	private final Boolean startTLSEnabled;
	
	public EmailServerSettings(Builder builder) {
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
		
		public EmailServerSettings build() {
			return new EmailServerSettings(this);
		}
	}
}
