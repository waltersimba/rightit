package co.za.rightit.messaging.web.api;

import java.util.Optional;

import co.za.rightit.messaging.email.EmailOptions;

public interface EmailOptionsRepository {
	Optional<EmailOptions> findEmailOptions(String username); 
}
