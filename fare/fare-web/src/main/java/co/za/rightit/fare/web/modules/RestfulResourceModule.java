package co.za.rightit.fare.web.modules;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import co.za.rightit.fare.web.cors.CORSFilter;

public class RestfulResourceModule extends ServletModule {

	@Override
	public void configureServlets() {
		ResourceConfig rc = new PackagesResourceConfig("co.za.rightit.fare.web.resources");
		for (Class<?> resource : rc.getClasses()) {
			bind(resource);
		}
		final Map<String, String> options = new HashMap<String, String>();
		options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		options.put("com.sun.jersey.config.feature.DisableWADL", "true");
		options.put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, Joiner.on(",")
				.join(GZIPContentEncodingFilter.class.getName(), CORSFilter.class.getName()));
		options.put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, GZIPContentEncodingFilter.class.getName());				

		serve("/api/*").with(GuiceContainer.class, options);
	}
	
}
