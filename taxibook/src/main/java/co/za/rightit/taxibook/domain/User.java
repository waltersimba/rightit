package co.za.rightit.taxibook.domain;

public class User extends DomainObject {
	
	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private String hashedPassword;
	
	private Role role;
	
	private boolean verified;
	
	public User() {
		super();
	}
	
	public User(UserBuilder builder) {
		super("user");
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.emailAddress = builder.emailAddress;
		this.hashedPassword = builder.hashedPassword;
		this.role = builder.role;
		this.verified = builder.verified;
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
		
	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public static class UserBuilder {
		private String hashedPassword;
		private String emailAddress;
		private String firstName;
		private String lastName;
		private Role role;
		private boolean verified;
		
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
		
		public UserBuilder withVerified(boolean verified) {
			this.verified = verified;
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
		
		@Override
		public String toString() {
			return name;
		}
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", emailAddress=" + emailAddress
				+ ", hashedPassword=" + hashedPassword + ", role=" + role + ", verified=" + verified + ", getId()="
				+ getId() + "]";
	}
		
}
