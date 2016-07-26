package co.za.rightit.taxibook.service.verify;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;

@XmlRootElement
public class EmailBasedRequest {
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
