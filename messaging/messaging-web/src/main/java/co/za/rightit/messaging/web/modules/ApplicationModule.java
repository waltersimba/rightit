package co.za.rightit.messaging.web.modules;

import com.google.inject.AbstractModule;

import co.za.rightit.messaging.web.model.ApplicationOptions;

public class ApplicationModule extends AbstractModule {

	private final ApplicationOptions options;
	
	public ApplicationModule(ApplicationOptions options) {
		this.options = options;
	}
	
	@Override
	protected void configure() {
		bind(ApplicationOptions.class).toInstance(options);
	}

}
