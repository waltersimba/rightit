package co.za.rightit.messaging.email;

import org.apache.commons.mail.EmailException;

public interface EmailService {

	void send(EmailMessage emailMessage) throws EmailException;
}
