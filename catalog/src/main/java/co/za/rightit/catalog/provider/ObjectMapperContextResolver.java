package co.za.rightit.catalog.provider;

import java.io.IOException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        mapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Link.class, new LinkSerializer());
        mapper.registerModule(simpleModule);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
    
    private class LinkSerializer extends JsonSerializer<Link> {

    	@Override
    	public void serialize(Link link, JsonGenerator jg, SerializerProvider sp)
    			throws IOException, JsonProcessingException {
    		jg.writeStartObject();
    		jg.writeStringField("rel", link.getRel());
    		jg.writeStringField("href", link.getUri().toString());
    		jg.writeEndObject();
    	}
    }

}
