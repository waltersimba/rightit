package co.za.rightit.commons.utils.guice;

import com.google.inject.AbstractModule;

import co.za.rightit.commons.utils.CleanupHandler;

public class LifecycleModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CleanupHandler.class);		
	}

}
