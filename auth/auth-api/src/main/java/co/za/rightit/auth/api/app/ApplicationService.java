package co.za.rightit.auth.api.app;

import java.util.Optional;

import com.sun.research.ws.wadl.Application;

import co.za.rightit.auth.model.app.CreateAppRequest;
import co.za.rightit.auth.model.app.CreateAppResponse;
import co.za.rightit.auth.model.credential.ApiKey;

public interface ApplicationService {
	CreateAppResponse createApp(CreateAppRequest request);
	Optional<Application> findByApiKey(ApiKey apiKey);
}
