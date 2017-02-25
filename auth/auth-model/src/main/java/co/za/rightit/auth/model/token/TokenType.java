package co.za.rightit.auth.model.token;

public enum TokenType {
	Bearer("Bearer"),
	Basic("Basic");
	
	private String value;
	
	private TokenType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
