package co.za.rightit.taxibook.provider;

import java.io.File;

import javax.inject.Provider;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigurationProvider implements Provider<Configuration> {
	private static final String CONFIGURATION_FILE = "application.properties";

	@Override
	public Configuration get() {
		return loadConfigurations();
	}

	private Configuration loadConfigurations() {
		PropertiesConfiguration configuration = null;
		if (new File(CONFIGURATION_FILE).canRead()) {
			try {
				configuration = new PropertiesConfiguration(CONFIGURATION_FILE);
			} catch (ConfigurationException ex) {
				// log error
			}
		}
		return configuration;
	}

}
