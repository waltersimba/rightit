package co.za.rightit.healthchecks.model;

import java.util.Map;

public class Node {

	private String name;

	private Map<String, Object> properties;

	public Node() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public Object get(String key) {
		return properties.get(key);
	}
		
	public void put(String key, Object value) {
		properties.put(key, value);
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Node withName(String name) {
		setName(name);
		return this;
	}

	public Node withProperties(Map<String, Object> properties) {
		setProperties(properties);
		return this;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + ", properties=" + properties + "]";
	}

}
