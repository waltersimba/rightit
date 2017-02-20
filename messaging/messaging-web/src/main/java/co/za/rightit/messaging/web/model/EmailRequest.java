package co.za.rightit.messaging.web.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;

@XmlRootElement
public class EmailRequest {
	
	@NotNull(message = "The email address is required")
    @Email(message = "Email is not a valid")
	private String to;
	@NotNull(message = "Message is required")
	private String message;
	@NotNull(message = "Phone number is required")
	private String phoneNumber;
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}	
