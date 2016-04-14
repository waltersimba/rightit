package com.rightit.taxibook.domain;

public class User extends DomainObject {
	
	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private String hashedPassword;
	
	private Role role;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public enum Role {
		ADMINISTRATOR("Administrator"),
		DRIVER("Driver"),
		OWNER("Owner"),
		OWNER_DRIVER("Owner/Driver");
		
		private String name;
		
		private Role(String name) {
			this.name = name;
		};
		
		public Role fromString(String roleString) {
			Role roleFound = null;
			for(Role role : values()) {
				if(role.name.equals(roleString)) {
					roleFound = role;
					break;
				}
			}
			if(roleFound == null) {
				throw new IllegalArgumentException("Invalid role speficified");
			}
			return roleFound;
		}
		
		public String getName() {
			return name;
		}
	}
}
