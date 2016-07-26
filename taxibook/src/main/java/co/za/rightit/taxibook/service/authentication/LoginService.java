package co.za.rightit.taxibook.service.authentication;

import java.util.concurrent.CompletableFuture;

public interface LoginService {
	
	CompletableFuture<AuthenticationToken> login(LoginRequest request);
}
