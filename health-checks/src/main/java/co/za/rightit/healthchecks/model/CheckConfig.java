package co.za.rightit.healthchecks.model;

import java.util.List;

import com.google.common.base.Optional;

public class CheckConfig {

	private String name;

	private List<Node> nodes;

	public CheckConfig() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public CheckConfig withName(String name) {
		setName(name);
		return this;
	}

	public CheckConfig withNodes(List<Node> nodes) {
		setNodes(nodes);
		return this;
	}

	public Optional<Node> getNode(String name) {
		Optional<Node> optional = Optional.absent();
		for(Node current : nodes) {
			if(current.getName().equals(name)) {
				optional = Optional.of(current);
				break;
			}
		}
		return optional;
	}

	public int getNodeIndex(String nodeName) {
		Optional<Node> optional = getNode(nodeName);
		if(optional.isPresent()) {
			return nodes.indexOf(optional.get());
		} else {
			return -1;
		}
	}

}
