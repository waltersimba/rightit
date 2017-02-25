package co.za.rightit.auth.api.app;

import co.za.rightit.auth.model.app.CreateAppRequest;
import co.za.rightit.auth.model.app.CreateAppResponse;

public interface ApplicationService {
	CreateAppResponse createApp(CreateAppRequest request);
}
