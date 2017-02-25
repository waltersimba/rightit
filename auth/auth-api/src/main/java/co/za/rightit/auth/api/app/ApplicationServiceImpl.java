package co.za.rightit.auth.api.app;

import co.za.rightit.auth.model.app.CreateAppRequest;
import co.za.rightit.auth.model.app.CreateAppResponse;

public class ApplicationServiceImpl implements ApplicationService {

	@Override
	public CreateAppResponse createApp(CreateAppRequest request) {
		// TODO Check that application name is unique
		// TODO persist Application model
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
