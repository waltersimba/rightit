package co.za.rightit.auth.model.basic;

import java.util.ArrayList;
import java.util.List;

public class BasicPrincipal {
	
	private String username;
	/**
	 * Used to check authorization to perform actions.
	 */
	private List<String> roles = new ArrayList<>();
	
	public BasicPrincipal(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	 
	public List<String> getRoles() {
		return roles;
	}
	
	public boolean isUserInRole(String roleToCheck) {
		return roles.contains(roleToCheck);
	}
	
}
