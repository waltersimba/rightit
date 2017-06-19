package co.za.rightit.messaging.web.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;

import com.google.common.base.MoreObjects;

@XmlRootElement
public class EmailContactUsReply implements EmailData {

	@NotNull(message = "The email address is required")
	@Email(message = "Email is not a valid")
	private String to;
	@NotNull(message = "Contact name is required")
	private String contactName;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public EmailContactUsReply withTo(String to) {
		setTo(to);
		return this;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public EmailContactUsReply withContactName(String contactName) {
		setContactName(contactName);
		return this;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("contactName", contactName).add("to", to).toString();
	}

}
