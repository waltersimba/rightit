package com.rightit.taxibook.service;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.validation.Enum;

@XmlRootElement
public class CreateUserRequest {
	@Length(min=8, max=30)
    @NotNull
	private String password;
	
	@NotNull
    @Email
	private String emailAddress;
	
	@Length(max=50)
	private String firstName;
	
	@Length(max=50)
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
