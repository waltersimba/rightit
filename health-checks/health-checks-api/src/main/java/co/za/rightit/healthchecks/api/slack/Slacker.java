package co.za.rightit.healthchecks.api.slack;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class Slacker {

	private final WebResource slackMeResource;
	private String channel;
	private String screenName;
	private String iconUrl;

	public Slacker(String webhookPath) {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		slackMeResource = client.resource("https://hooks.slack.com:443").path(webhookPath);
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
		ClientResponse response = slackMeResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,
				slackMessage);
		return response.getStatus() == Response.Status.OK.getStatusCode();
	}

	public SlackMessage toSlackMessage(String message, SlackMessage.Color color) {
		Preconditions.checkArgument(screenName != null, "Screen name cannot be null");
		Preconditions.checkArgument(channel != null, "NotificationChannel cannot be null");
		return new SlackMessage(screenName, iconUrl, channel, message, color);
	}

}
