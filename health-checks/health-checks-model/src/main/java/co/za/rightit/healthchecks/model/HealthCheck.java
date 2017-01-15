package co.za.rightit.healthchecks.model;

import java.util.Map;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import org.joda.time.DateTime;

public class HealthCheck {
	
	@MongoId
	@MongoObjectId 
    private String id;
	private String name;
	private String type;
	private Map<String, Object> properties;
	
	public HealthCheck() {}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public Integer getInt(String key) {
		return (Integer)properties.get(key);
	}
	
	public String getString(String key) {
		return (String)properties.get(key);
	}
	
	public Boolean getBoolean(String key) {
		return (Boolean)properties.get(key);
	}
	
	public DateTime getDateTime(String key) {
		return DateTime.parse(getString(key));
	}
	
	public void put(String key, Object value) {
		properties.put(key, value);	
	}

	public HealthCheck withName(String name) {
		this.name = name;
		return this;
	}
	
	public HealthCheck withType(String type) {
		this.type = type;
		return this;
	}
	
	public HealthCheck withProperties(Map<String, Object> properties) {
		this.properties = properties;
		return this;
	}

	public enum Type {
		URL("url"),
		NGINX("nginx"),
		MONGO_DB("mongodb"),
		MEMCACHED("memcached");
		
		private String identifier;
		
		private Type(String identifier) {
			this.identifier = identifier;
		}
		
		public String getIdentifier() {
			return identifier;
		}
	}

	public enum Status {
		NEW("new"),
		PAUSED("paused"),
		UP("up"),
		LATE("late"),
		DOWN("down");
		
		private String identifier;
		
		private Status(String identifier) {
			this.identifier = identifier;
		}
		
		public String getIdentifier() {
			return identifier;
		}		
	}

}
