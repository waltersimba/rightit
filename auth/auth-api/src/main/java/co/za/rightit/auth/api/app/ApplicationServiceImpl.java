package co.za.rightit.auth.api.app;

import java.util.Optional;

import com.sun.research.ws.wadl.Application;

import co.za.rightit.auth.model.app.CreateAppRequest;
import co.za.rightit.auth.model.app.CreateAppResponse;
import co.za.rightit.auth.model.credential.ApiKey;

public class ApplicationServiceImpl implements ApplicationService {

	@Override
	public CreateAppResponse createApp(CreateAppRequest request) {
		// TODO Check that application name is unique
		// TODO persist Application model
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Optional<Application> findByApiKey(ApiKey apiKey) {
		return null;
	}

}
