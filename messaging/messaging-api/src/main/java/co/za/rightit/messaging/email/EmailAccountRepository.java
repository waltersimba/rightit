package co.za.rightit.messaging.email;

import java.util.Optional;

public interface EmailAccountRepository {
	Optional<EmailAccount> findEmailAccount(String domain); 
}
