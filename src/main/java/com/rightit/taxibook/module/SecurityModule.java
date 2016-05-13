package com.rightit.taxibook.module;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.servlet.ServletModule;
import com.rightit.taxibook.security.ResourceFilterFactory;
import com.rightit.taxibook.security.BearerTokenFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class SecurityModule extends ServletModule {

	@Override 
	 protected void configureServlets() { 
		final Map<String, String> params = new HashMap<String, String>(); 
        
		params.put(PackagesResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, BearerTokenFilter.class.getName());
         
        params.put(PackagesResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES, ResourceFilterFactory.class.getName()); 
         
        filter("/*").through(GuiceContainer.class, params); 
	}
}
