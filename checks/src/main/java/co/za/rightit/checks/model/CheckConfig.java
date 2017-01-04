package co.za.rightit.checks.model;

import java.util.List;

public class CheckConfig {

	private String name;

	private List<CheckNodeConfig> nodes;

	public CheckConfig() {
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<CheckNodeConfig> getNodes() {
		return nodes;
	}
	
	public void setNodes(List<CheckNodeConfig> nodes) {
		this.nodes = nodes;
	}

	public CheckConfig withName(String name) {
		setName(name);
		return this;
	}

	public CheckConfig withNodes(List<CheckNodeConfig> nodes) {
		setNodes(nodes);
		return this;
	}

}
