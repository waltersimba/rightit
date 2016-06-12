package com.rightit.taxibook.module;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.guice.web.ShiroWebModule;

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
				
		 final Map<String, String> params = new HashMap<String, String>();

         params.put(PackagesResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,"com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory");
		
		serve( "/api/*" ).with( GuiceContainer.class, params);
		
		install(ShiroWebModule.guiceFilterModule());
	}
}
