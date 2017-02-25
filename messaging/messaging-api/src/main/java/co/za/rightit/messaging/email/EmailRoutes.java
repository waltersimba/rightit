package co.za.rightit.messaging.email;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.google.inject.Inject;

public class EmailRoutes extends RouteBuilder {

	@Inject
	private EmailService emailService;
	
	@Override
	public void configure() throws Exception {
		from("seda:email?concurrentConsumers=5").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				EmailMessage message = exchange.getIn().getBody(EmailMessage.class);
				emailService.send(message);
			}
		});
	}

}
