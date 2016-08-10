package co.za.rightit.catalog.module;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import co.za.rightit.commons.provider.ObjectMapperContextResolver;

public class RestfulResourceModule extends ServletModule {

	@Override
	public void configureServlets() {
		ResourceConfig rc = new PackagesResourceConfig("co.za.rightit.catalog.resources");
		bind(ObjectMapperContextResolver.class).asEagerSingleton();
		for (Class<?> resource : rc.getClasses()) {
			bind(resource);
		}

		final Map<String, String> params = new HashMap<String, String>();

		serve("/api/*").with(GuiceContainer.class, params);
	}
}
