package co.za.rightit.commons.provider;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.inject.Provider;

public class ObjectMapperProvider implements Provider<ObjectMapper> {

	private ObjectMapper objectMapper = null;
	
	public ObjectMapperProvider() {
		this.objectMapper = createObjectMapper();
	}
	
	@Override
	public ObjectMapper get() {
		return objectMapper;
	}

	public ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		SimpleModule simpleModule = new SimpleModule();
        //simpleModule.addSerializer(Link.class, new LinkSerializer());
        simpleModule.addSerializer(ObjectId.class, new ObjectIdJsonSerializer());
        simpleModule.addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
		objectMapper.registerModule(simpleModule);
		objectMapper.registerModule(new JodaModule());
		return objectMapper;
	}

	private static class ObjectIdJsonDeserializer extends JsonDeserializer<ObjectId> {
		@Override
		public ObjectId deserialize(JsonParser jsonParser, DeserializationContext DesearializationCtxt)
				throws IOException {
			final ObjectCodec objectCodec = jsonParser.getCodec();
			final JsonNode node = objectCodec.readTree(jsonParser);
			if (node.isNull() || !node.has("$oid") || node.get("$oid").isNull()) {
				return null;
			}
			return new ObjectId(node.get("$oid").asText());
		}
	}
	
	private class ObjectIdJsonSerializer extends JsonSerializer<ObjectId> {
	    @Override
	    public void serialize(ObjectId objectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
	        if (objectId == null) {
	            jsonGenerator.writeNull();
	        } else {
	            jsonGenerator.writeString(objectId.toString());
	        }
	    }
	}
	/*
	private class LinkSerializer extends JsonSerializer<Link> {

    	@Override
    	public void serialize(Link link, JsonGenerator jg, SerializerProvider sp)
    			throws IOException, JsonProcessingException {
    		jg.writeStartObject();
    		jg.writeStringField("rel", link.getRel());
    		jg.writeStringField("href", link.getUri().toString());
    		jg.writeEndObject();
    	}
    }*/

}
