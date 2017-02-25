package co.za.rightit.auth.api.token;

import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.sun.research.ws.wadl.Application;

import co.za.rightit.auth.api.app.ApplicationService;
import co.za.rightit.auth.model.credential.ApiKey;
import co.za.rightit.auth.model.token.AccessTokenRequest;

public class TokenServiceImpl implements TokenService {

	private final TokenGenerator<Application> tokenGenerator;
	private final ApplicationService applicationService;

	@Inject
	public TokenServiceImpl(TokenGenerator<Application> tokenGenerator, ApplicationService applicationService) {
		this.tokenGenerator = Preconditions.checkNotNull(tokenGenerator, "tokenGenerator");
		this.applicationService = Preconditions.checkNotNull(applicationService, "applicationService");
	}

	@Override
	public String getAccessToken(AccessTokenRequest request) {
		Optional<Application> optional = applicationService.findByApiKey(
				new ApiKey(request.getClientId(), request.getSecret()));
		if(optional.isPresent()) {
			return tokenGenerator.createToken(optional.get());
		} else {
			throw new UnsupportedOperationException("Not yet implemented");
		}
	}

}
