package com.rightit.taxibook.provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Singleton
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {
	
	@Inject
	private ObjectMapperProvider provider;
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		// TODO Auto-generated method stub
		return provider.get();
	}

}
