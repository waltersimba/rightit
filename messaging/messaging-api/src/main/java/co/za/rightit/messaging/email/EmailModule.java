package co.za.rightit.messaging.email;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.guice.CamelModuleWithRouteTypes;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;

public class EmailModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EmailService.class).to(EmailServiceImpl.class);
		bind(EmailEventSubscriber.class).asEagerSingleton();
		install(new CamelModuleWithRouteTypes(
				ImmutableSet.<Class<? extends RoutesBuilder>>builder()
				.add(EmailRoutes.class)
				.build()));
	}

}
