package com.rightit.taxibook.module;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ResourceModule extends ServletModule {

	@Override
	public void configureServlets() {
		ResourceConfig rc = new PackagesResourceConfig("com.rightit.taxibook");
		for (Class<?> resource : rc.getClasses()) {
			bind(resource);
		}
		
		serve( "/api/*" ).with( GuiceContainer.class );
	}
}
