package com.rightit.taxibook.service.authentication;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.security.JWTPrincipal;

public interface TokenAuthenticationService {
	
	String generateToken(User user);
	
	JWTPrincipal authenticateToken(String token);
}
