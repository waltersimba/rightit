package co.za.rightit.catalog.module;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import co.za.rightit.commons.provider.JacksonJsonProviderProvider;
import co.za.rightit.commons.provider.ObjectMapperProvider;

public class RestfulResourceModule extends ServletModule {

	@Override
	public void configureServlets() {
		ResourceConfig rc = new PackagesResourceConfig("co.za.rightit.catalog.resources");
		bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
		bind(JacksonJsonProvider.class).toProvider(JacksonJsonProviderProvider.class).in(Singleton.class);
		for (Class<?> resource : rc.getClasses()) {
			bind(resource);
		}
		final Map<String, String> options = new HashMap<String, String>();
		options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		options.put("com.sun.jersey.config.feature.DisableWADL", "true");	
		serve("/api/*").with(GuiceContainer.class, options);
	}
}
