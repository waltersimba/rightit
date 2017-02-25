package co.za.rightit.messaging.web.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class CreateWebhookRequest {

	/**
	 * Webhook listener URL to receive HTTP POST notification messages when an event occurs.
	 */
	private String url;
	private List<String> eventTypes;
	
	public CreateWebhookRequest(String url, List<String> eventTypes) {
		this.url = Preconditions.checkNotNull(url, "url");
		this.eventTypes = Preconditions.checkNotNull(eventTypes, "eventTypes");
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@JsonProperty(value="event_types")
	public List<String> getEventTypes() {
		return eventTypes;
	}
	
	public void setEventTypes(List<String> eventTypes) {
		this.eventTypes = eventTypes;
	}
		
}
