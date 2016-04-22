package com.rightit.taxibook.service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

public class EmailVerificationRequest {
	@NotNull(message = "The email address is required")
    @Email(message = "Email address is not a valid")
	private String emailAddress;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
