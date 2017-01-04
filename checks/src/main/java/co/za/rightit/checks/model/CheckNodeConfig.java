package co.za.rightit.checks.model;

import java.util.Map;

public class CheckNodeConfig {

	private String name;

	private Map<String, Object> values;

	public CheckNodeConfig() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getValues() {
		return values;
	}
	
	public int getSize() {
		return values.size();
	}
	
	public Object getValue(String key) {
		return values.get(key);
	}
	
	public void putValue(String key, Object value) {
		values.put(key, value);
	}

	public void setValues(Map<String, Object> values) {
		this.values = values;
	}

	public CheckNodeConfig withName(String name) {
		setName(name);
		return this;
	}

	public CheckNodeConfig withValues(Map<String, Object> values) {
		setValues(values);
		return this;
	}

}
