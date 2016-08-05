package co.za.rightit.catalog.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("mongo.host")).to("127.0.0.1");
		bindConstant().annotatedWith(Names.named("mongo.port")).to(27017);
		bindConstant().annotatedWith(Names.named("mongo.database")).to("catalog");
	}

}
