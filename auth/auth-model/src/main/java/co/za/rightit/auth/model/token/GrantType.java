package co.za.rightit.auth.model.token;

public enum GrantType {
	CLIENT_CREDENTIALS("client_credentials");

	private String value;

	private GrantType(String value) {
		this.value = value;
	}

	public static GrantType from(String valueToCheck) {
		for (GrantType grantType : values()) {
			if (grantType.value.equals(valueToCheck)) {
				return grantType;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid value for enum: ", valueToCheck));
	}

	public String getValue() {
		return value;
	}

}
