package com.rightit.taxibook.service.token;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.rightit.taxibook.domain.VerificationToken;

public interface VerificationTokenService {
	
	CompletableFuture<Optional<VerificationToken>> generateEmailVerificationToken(EmailVerificationRequest request);
	
	Optional<VerificationToken> verify(String token);	
}
