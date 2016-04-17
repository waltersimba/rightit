package com.rightit.taxibook.provider;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
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

	@Override
	public ObjectMapper get() {
		return createObjectMapper();
	}

	public ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		SimpleModule module = new SimpleModule();
		module.addSerializer(ObjectId.class, new ObjectIdJsonSerializer());
		module.addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
		mapper.registerModule(module);
		mapper.registerModule(new JodaModule());
		return mapper;
	}

	public class ObjectIdJsonSerializer extends JsonSerializer<ObjectId> {
		@Override
		public void serialize(ObjectId objectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
				throws IOException {
			if (objectId == null) {
				jsonGenerator.writeNull();
			} else {
				jsonGenerator.writeStartObject();
				jsonGenerator.writeStringField("$oid", objectId.toString());
				jsonGenerator.writeEndObject();
			}
		}
	}

	public class ObjectIdJsonDeserializer extends JsonDeserializer<ObjectId> {
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
}
