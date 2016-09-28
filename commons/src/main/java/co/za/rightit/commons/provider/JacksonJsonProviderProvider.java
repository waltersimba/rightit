package co.za.rightit.commons.provider;

import javax.inject.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Inject;

public class JacksonJsonProviderProvider implements Provider<JacksonJsonProvider> {
	private final ObjectMapper mapper;

    @Inject
    JacksonJsonProviderProvider(ObjectMapper mapper) {
       this.mapper = mapper;
    }

    @Override
    public JacksonJsonProvider get() {
       return new JacksonJsonProvider(mapper);
    }
}