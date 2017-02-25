package co.za.rightit.auth.model.credential;

public enum CredentialType {
	API("api"),
	BASIC("basic");
	
	private String value;
	
	private CredentialType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
