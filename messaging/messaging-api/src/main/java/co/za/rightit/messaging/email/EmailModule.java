package co.za.rightit.messaging.email;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.guice.CamelModuleWithRouteTypes;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class EmailModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EmailService.class).to(EmailServiceImpl.class);
		bind(EmailEventSubscriber.class).in(Singleton.class);
		install(new CamelModuleWithRouteTypes(
				ImmutableSet.<Class<? extends RoutesBuilder>>builder()
				.add(EmailRouteBuilder.class)
				.build()));
	}

}
