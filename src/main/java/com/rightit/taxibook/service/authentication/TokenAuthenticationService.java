package com.rightit.taxibook.service.authentication;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.security.UserInfo;

public interface TokenAuthenticationService {
	
	String generateToken(User user);
	
	UserInfo authenticateToken(String token);
}
