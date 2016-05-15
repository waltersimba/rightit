package com.rightit.taxibook.service.verify;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.joda.time.DateTime;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.domain.VerificationToken.VerificationTokenType;

public interface TokenGenerator {
	
	CompletableFuture<Optional<VerificationToken>> generateVerificationToken(User user, VerificationTokenType tokenType, DateTime expires);
}
