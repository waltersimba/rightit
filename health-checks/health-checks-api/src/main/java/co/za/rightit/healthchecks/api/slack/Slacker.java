package co.za.rightit.healthchecks.api.slack;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

import com.google.common.base.Preconditions;

public class Slacker {

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

	public boolean sendSlackMessage(String message, SlackMessage.Color color) {
		return sendSlackMessage(toSlackMessage(message, color));
	}
	
	public boolean sendSlackMessage(SlackMessage slackMessage) {
		final Response response = slackMeResource
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.json(slackMessage));
		return response.getStatus() == Response.Status.OK.getStatusCode();
	}

	public SlackMessage toSlackMessage(String message, SlackMessage.Color color) {
		Preconditions.checkArgument(screenName != null, "Screen name cannot be null");
		Preconditions.checkArgument(channel != null, "NotificationChannel cannot be null");
		return new SlackMessage(screenName, iconUrl, channel, message, color);
	}

}
