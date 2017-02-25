package co.za.rightit.auth.model.app;

import org.joda.time.DateTime;

import co.za.rightit.auth.model.credential.ApiCredential;

public class Application {
	
	private String name;
	
	private ApiCredential credential;
	
	private DateTime created;
	
	private boolean active;
	
	public Application() {
		this.created = DateTime.now();
	}

	public Application withName(String name) {
		this.name = name;
		return this;
	}
	
	public Application withCredentials(ApiCredential credential) {
		this.credential = credential;
		return this;
	}
	
	public Application withCreated(DateTime created) {
		this.created = created;
		return this;
	}
	
	public Application withIsActive(boolean active) {
		this.active = active;
		return this;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ApiCredential getCredential() {
		return credential;
	}

	public void setCredentials(ApiCredential credential) {
		this.credential = credential;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
