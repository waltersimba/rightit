package co.za.rightit.catalog.bot;

import org.apache.camel.builder.RouteBuilder;

public class BotRouteBuilder extends RouteBuilder {
	
	private static final String BOT_ENDPOINT = "telegram:bots/262550354:AAHn-_qnOito77Syfx8VozE36Jlqi0iLwZw"; 
	
	@Override
	public void configure() throws Exception {
		from(BOT_ENDPOINT)
		.bean(Bot.class)
		.to(BOT_ENDPOINT);
	}

}
