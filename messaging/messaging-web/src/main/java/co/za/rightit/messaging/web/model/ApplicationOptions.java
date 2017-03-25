package co.za.rightit.messaging.web.model;

import java.nio.file.Path;

public class ApplicationOptions {
	
	private final Path emailAccountsFile;
	
	public ApplicationOptions(Builder builder) {
		this.emailAccountsFile = builder.emailAccountsFile;
	}
	
	public Path getEmailAccountsFile() {
		return emailAccountsFile;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static class Builder {
		private Path emailAccountsFile;
		
		public Builder withEmailAccountsFile(Path emailAccountsFile) {
			this.emailAccountsFile = emailAccountsFile;
			return this;
		}
		
		public ApplicationOptions build() {
			return new ApplicationOptions(this);
		}
	}
}
