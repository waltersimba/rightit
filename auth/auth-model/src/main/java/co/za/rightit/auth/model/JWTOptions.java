package co.za.rightit.auth.model;

public class JWTOptions {

	private final Integer ttlInMinutes;
	private final String issuer;

	public JWTOptions(Builder builder) {
		ttlInMinutes = builder.ttlInMinutes;
		issuer = builder.issuer;
	}

	public Integer getTtlInMinutes() {
		return ttlInMinutes;
	}

	public String getIssuer() {
		return issuer;
	}

	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static class Builder {

		private Integer ttlInMinutes;
		private String issuer;

		public Builder withTtlInMinutes(Integer ttlInMinutes) {
			this.ttlInMinutes = ttlInMinutes;
			return this;
		}

		public Builder withIssuer(String issuer) {
			this.issuer = issuer;
			return this;
		}

		public JWTOptions build() {
			return new JWTOptions(this);
		}

	}
}
