package co.za.rightit.taxibook.service.authentication;

import java.util.concurrent.CompletableFuture;

import co.za.rightit.taxibook.domain.User;
import co.za.rightit.taxibook.security.JWTPrincipal;

public interface TokenAuthenticationService {
	
	CompletableFuture<String> generateToken(User user);
	
	JWTPrincipal authenticateToken(String token);
}
