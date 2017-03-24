package co.za.rightit.messaging.web.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;

import com.google.common.base.MoreObjects;

@XmlRootElement
public class ContactRequest {
	
	@NotNull(message = "The email address is required")
    @Email(message = "Email is not a valid")
	private String to;
	@NotNull(message = "Message is required")
	private String message;
	@NotNull(message = "Contact name is required")
	private String contactName;
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
	
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("to", to)
				.add("message", message)
				.add("phoneNumber", phoneNumber)
				.add("contactName", contactName)
				.toString();
	}
	
}	
