package co.za.rightit.taxibook.service.user;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import co.za.rightit.taxibook.domain.User;
import co.za.rightit.taxibook.validation.Enum;

@XmlRootElement
public class CreateUserRequest {
	@Length(min=8, max=30, message = "The password must be between {min} and {max} characters")
    @NotNull(message = "The password is required")
	private String password;
	
	@NotNull(message = "The email address is required")
    @Email(message = "Email is not a valid")
	private String emailAddress;
	
	@Length(max=50, message = "First name shoud not be higher than {max}")
	private String firstName;
	
	@Length(max=50, message = "Last name shoud not be higher than {max}")
	private String lastName;
	
	@Enum(enumClass=User.Role.class, ignoreCase=true) 
	private String role;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
