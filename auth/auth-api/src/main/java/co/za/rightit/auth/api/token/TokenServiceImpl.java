package co.za.rightit.auth.api.token;

import co.za.rightit.auth.model.token.AccessTokenRequest;
import co.za.rightit.auth.model.token.AccessTokenResponse;

public class TokenServiceImpl implements TokenService {

	@Override
	public AccessTokenResponse getAccessToken(AccessTokenRequest request) {
		// TODO: Verify credentials
		// TODO: Generate and return an access token
		return null;
	}

}
