package com.rightit.taxibook.service.authentication;

import java.util.concurrent.CompletableFuture;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.security.JWTPrincipal;

public interface TokenAuthenticationService {
	
	CompletableFuture<String> generateToken(User user);
	
	JWTPrincipal authenticateToken(String token);
}
