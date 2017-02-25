package co.za.rightit.auth.api.token;

import co.za.rightit.auth.model.token.AccessTokenRequest;
import co.za.rightit.auth.model.token.AccessTokenResponse;

public interface TokenService {
	AccessTokenResponse getAccessToken(AccessTokenRequest request);
}
