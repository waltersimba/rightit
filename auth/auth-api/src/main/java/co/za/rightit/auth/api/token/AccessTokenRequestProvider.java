package co.za.rightit.auth.api.token;

import com.google.inject.Provider;

import co.za.rightit.auth.model.token.AccessTokenRequest;

public class AccessTokenRequestProvider implements Provider<AccessTokenRequest> {

	@Override
	public AccessTokenRequest get() {
		// TODO: Process HTTP header 'Authorization <Token-Type> <Access-Token>'
		return null;
	}

}
