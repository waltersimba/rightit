package co.za.rightit.geolocation.web.modules;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class RestfulResourceModule extends ServletModule {

	@Override
	public void configureServlets() {
		ResourceConfig rc = new PackagesResourceConfig("co.za.rightit.geolocation.web.resources");
		for (Class<?> resource : rc.getClasses()) {
			bind(resource);
		}
		final Map<String, String> options = new HashMap<String, String>();
		options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		options.put("com.sun.jersey.config.feature.DisableWADL", "true");	
		serve("/api/*").with(GuiceContainer.class, options);
	}
}
