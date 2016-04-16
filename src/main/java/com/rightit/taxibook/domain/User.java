package com.rightit.taxibook.domain;

public class User extends DomainObject {
	
	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private String hashedPassword;
	
	private Role role;
	
	public User(UserBuilder builder) {
		super();
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.emailAddress = builder.emailAddress;
		this.hashedPassword = builder.hashedPassword;
		this.role = builder.role;
	}
	
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
		
	public static class UserBuilder {
		private String hashedPassword;
		private String emailAddress;
		private String firstName;
		private String lastName;
		private Role role;
		
		public UserBuilder withHashedPassword(String hashedPassword) {
			this.hashedPassword = hashedPassword;
			return this;
		}
		
		public UserBuilder withEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
			return this;
		}
		
		public UserBuilder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public UserBuilder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public UserBuilder withRole(Role role) {
			this.role = role;
			return this;
		}
		
		public User build() {
			return new User(this);
		}
	
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
		
		public static Role fromString(String roleString) {
			Role roleFound = null;
			for(Role role : Role.values()) {
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
