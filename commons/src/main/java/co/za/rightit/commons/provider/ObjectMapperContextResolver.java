package co.za.rightit.commons.provider;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
	
	@Inject
	private ObjectMapperProvider provider;
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return provider.get();
	}

}
