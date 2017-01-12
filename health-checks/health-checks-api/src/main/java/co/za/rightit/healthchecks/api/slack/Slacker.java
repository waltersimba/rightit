package co.za.rightit.healthchecks.api.slack;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import co.za.rightit.healthchecks.model.events.SystemStateChangedEvent;

public class Slacker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Slacker.class);
	private final WebTarget slackMeResource;
	private String channel;
    private String screenName;
    private String iconUrl;
        
    public Slacker(String webhookPath) {
    	final Client client = ClientBuilder.newClient();
        UriBuilder uriBuilder = new JerseyUriBuilder();
        uriBuilder
                .scheme("https")
                .host("hooks.slack.com")
                .port(443)
                .path(webhookPath);
        slackMeResource = client.target(uriBuilder.build());
    }
    
    public Slacker withChannel(String channel) {
    	this.channel = channel;
    	return this;
    }
    
    public Slacker withScreenName(String screenName) {
    	this.screenName = screenName;
    	return this;
    }
    
    public Slacker withIconUrl(String iconUrl) {
    	this.iconUrl = iconUrl;
    	return this;
    }
    
	
    @Subscribe
    @AllowConcurrentEvents
    public void handle(SystemStateChangedEvent event) {
    	Preconditions.checkArgument(event != null, "SystemStateChangedEvent cannot be null");
    	
    	LOGGER.debug("Sending event to slack: {}" ,event.toString());
    	
    	String message = "Component health status changed:\n\n" + Preconditions.checkNotNull(event.getMessage(), "Message cannot be null");
    	
    	Response response = slackMeResource
    		.request(MediaType.APPLICATION_JSON_TYPE)
    		.post(Entity.json(toSlackMessage(message, event.isHealthy() ? SlackMessage.Color.GOOD : SlackMessage.Color.DANGER)));
    	if(response.getStatus() == Response.Status.OK.getStatusCode()) {
    		LOGGER.info("Message sent to slack successfully!");
    	} else {
    		LOGGER.warn("Slack could not process message, error code: {}.", response.getStatus());
    	}
    }

    public SlackMessage toSlackMessage(String message, SlackMessage.Color color) {
    	Preconditions.checkArgument(screenName != null, "Screen name cannot be null");
    	Preconditions.checkArgument(channel != null, "Channel cannot be null");
    	return new SlackMessage(screenName, iconUrl, channel, message, color);
    }
    
    public static void main(String[] args) {
    	Slacker slacker = new Slacker("services/T2P74FSUC/B3Q9FSLTU/1GkfjfcbzPschHhLYfkXnwya")
    			.withScreenName("healthchecks-bot")
    			.withChannel("#health-checks");
    	slacker.handle(new SystemStateChangedEvent("http://www.zhm.co.za", true, "<http://www.zhm.co.za> is running."));
    }

}
