package co.za.rightit.taxibook.service.verify;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import co.za.rightit.taxibook.domain.VerificationToken;

public interface VerificationTokenService {
	
	CompletableFuture<Optional<VerificationToken>> generateEmailVerificationToken(EmailBasedRequest request);
	
	CompletableFuture<Optional<VerificationToken>> generateResetPasswordToken(EmailBasedRequest request);
	
	CompletableFuture<Optional<VerificationToken>> resetPassword(PasswordRequest request);
	
	CompletableFuture<Optional<VerificationToken>> verifyUser(TokenVerificationRequest request);	
}
