package co.za.rightit.catalog.module;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.guice.CamelModuleWithRouteTypes;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;

import co.za.rightit.catalog.bot.BotRouteBuilder;

public class BotModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new CamelModuleWithRouteTypes(
				ImmutableSet.<Class<? extends RoutesBuilder>>builder()
				.add(BotRouteBuilder.class)
				.build()));
	}

}
