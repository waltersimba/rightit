package co.za.rightit.messaging.web.model;

import java.util.List;

import co.za.rightit.commons.link.Link;

public class Webhook {
	
	private String id;
	private String url;
	private List<String> eventTypes;
	private List<Link> links;
	
	public Webhook withUrl(String url) {
		this.url = url;
		return this;
	}
	
	public Webhook withEventTypes(List<String> eventTypes) {
		this.eventTypes = eventTypes;
		return this;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<String> getEventTypes() {
		return eventTypes;
	}
	
	public void setEventTypes(List<String> eventTypes) {
		this.eventTypes = eventTypes;
	}
	
	public List<Link> getLinks() {
		return links;
	}
	
	public void setLinks(List<Link> links) {
		this.links = links;
	}
		
}
