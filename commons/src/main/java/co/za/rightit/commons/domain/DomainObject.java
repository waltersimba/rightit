package co.za.rightit.commons.domain;

import org.joda.time.DateTime;

public class DomainObject extends Identifiable {
	
	private String type;
	
	private DateTime created;

	private DateTime modified;
	
	public DomainObject() {
		super();
	}

	public DomainObject(String type) {
		this.type = type;
		this.created = new DateTime();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public DateTime getModified() {
		return modified;
	}

	public void setModified(DateTime modified) {
		this.modified = modified;
	}
}
