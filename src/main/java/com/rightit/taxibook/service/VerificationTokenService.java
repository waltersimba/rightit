package com.rightit.taxibook.service;

import java.util.Optional;

import com.rightit.taxibook.domain.VerificationToken;

public interface VerificationTokenService {
	
	Optional<VerificationToken> generateEmailVerificationToken(EmailVerificationRequest request);
	
	Optional<VerificationToken> verify(String token);	
}
