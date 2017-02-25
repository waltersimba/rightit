package co.za.rightit.auth.api.token;

public interface TokenGenerator<Credential> {
	String createToken(Credential credential);
}
