package co.za.rightit.auth.api.token;

import java.security.Key;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;

import com.google.inject.Inject;

import co.za.rightit.auth.model.JWTOptions;
import co.za.rightit.auth.model.app.Application;
import co.za.rightit.auth.model.credential.ApiKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenGeneratorImpl implements TokenGenerator<Application> {

	private final JWTOptions options;
	
	@Inject
	public TokenGeneratorImpl(JWTOptions options) {
		this.options = options;
	}
	

	@Override
	public String createToken(Application application) {
		ApiKey apiKey = getApiKey(application);
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		Key signingKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(apiKey.getSecret()), 
				signatureAlgorithm.getJcaName());
		DateTime issuedAt = DateTime.now();
		return Jwts.builder()
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(issuedAt.toDate())
				.setNotBefore(issuedAt.toDate())
				.setSubject(application.getName())
				.setExpiration(DateTime.now().plusMinutes(options.getTtlInMinutes()).toDate())
				.claim("client_id", apiKey.getUsername())
				.setIssuer(options.getIssuer())
				.signWith(signatureAlgorithm, signingKey)
				.compact();
	}
	
	private ApiKey getApiKey(Application application) {
		return application.getCredential().getValue();
	}
	
}
