package co.za.rightit.auth.api.token;

import co.za.rightit.auth.model.token.AccessTokenRequest;

public interface TokenService {
	String getAccessToken(AccessTokenRequest request);
}
