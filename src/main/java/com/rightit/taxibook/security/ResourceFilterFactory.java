package com.rightit.taxibook.security;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;

import com.google.inject.Inject;
import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * FilterFactory to create List of request/response filters to be applied on a particular
 * AbstractMethod of a resource.
 */
@Singleton
@Provider
public class ResourceFilterFactory extends RolesAllowedResourceFilterFactory {
	
	@Inject
	private BearerTokenFilter bearerTokenFilter;
	
	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		List<ResourceFilter> rolesFilters = super.create(am);
		if (null == rolesFilters) {
			rolesFilters = new ArrayList<ResourceFilter>();
		}
		List<ResourceFilter> filters = new ArrayList<ResourceFilter>(rolesFilters);
		// Load SecurityContext first (this will load security context onto request)
		filters.add(0, bearerTokenFilter);
		return filters;
	}
}
