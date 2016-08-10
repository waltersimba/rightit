package co.za.rightit.taxibook.service.authentication;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.validation.Validator;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.za.rightit.commons.exceptions.ApplicationRuntimeException;
import co.za.rightit.commons.utils.FailedCompletableFutureBuilder;
import co.za.rightit.commons.utils.ValidationUtils;
import co.za.rightit.taxibook.domain.User;
import co.za.rightit.taxibook.domain.VerificationToken;
import co.za.rightit.taxibook.domain.VerificationToken.VerificationTokenType;
import co.za.rightit.taxibook.repository.UseRepository;
import co.za.rightit.taxibook.service.password.PasswordHashService;
import co.za.rightit.taxibook.service.verify.TokenGenerator;
import co.za.rightit.taxibook.spec.query.FindByEmailAddressAndPasswordSpec;
import co.za.rightit.taxibook.validation.exception.AuthenticationException;

public class LoginServiceImpl implements LoginService {

	private Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	private static final int REFRESH_TOKEN_EXPRIRY_IN_DAYS = 60;
	
	@Inject 
	private UseRepository userRepository;
		
	@Inject
	private TokenGenerator tokenGenerator;
	
	@Inject
	private JWTTokenService jwtTokenService;
	
	@Inject
	private PasswordHashService passwordHashService;
	
	@Inject
	private Validator validator;
	
	@Override
	public CompletableFuture<AuthenticationToken> login(LoginRequest request) {
		
		ValidationUtils.validate(request, validator);
		
		final String hashedPassword = passwordHashService.hashPassword(request.getPassword());
		return fetchUserByLoginCredentials(request.getUsername(), hashedPassword).thenCompose(user -> {
			return jwtTokenService.generateToken(user).thenCombine(createRefreshToken(user), (accessToken, refreshToken) -> {
				return new AuthenticationToken(accessToken, refreshToken);
			}); 
		});
	}
	
	private CompletableFuture<User> fetchUserByLoginCredentials(String username, String password) {
		LOGGER.info("Checking login credentials for user: {}...", username);
		final CompletableFuture<Optional<User>> futureOptionalUser = userRepository.findOne(
				new FindByEmailAddressAndPasswordSpec(username, password));
		return futureOptionalUser.thenCompose(optionalUser -> {
			CompletableFuture<User> future = new CompletableFuture<>();
			if (!optionalUser.isPresent()) {
				LOGGER.error(String.format("Failed to authenticate user: %s.", username));
				return new FailedCompletableFutureBuilder<User>().build(new AuthenticationException());
			} else {
				future.complete(optionalUser.get());
			}			
			return future; 
		});
	}
	
	private CompletableFuture<String> createRefreshToken(User user) {
		LOGGER.info("Creating refresh token for user: {}", user.getEmailAddress());
		return generateVerificationToken(user, VerificationTokenType.REFRESH_TOKEN).thenCompose(optionalToken -> {
			CompletableFuture<String> future = new CompletableFuture<>();
			if(!optionalToken.isPresent()) {
				String errorMessage = String.format("Failed to create refresh token for user: %s", user.getEmailAddress());
				return new FailedCompletableFutureBuilder<String>().build(new ApplicationRuntimeException(errorMessage));
			} else {
				future.complete(optionalToken.get().getToken());
			}
			return future;
		});
	}
	
	private CompletableFuture<Optional<VerificationToken>> generateVerificationToken(User user, VerificationTokenType tokenType) {
		return tokenGenerator.generateVerificationToken(user, tokenType, new DateTime().plusDays(REFRESH_TOKEN_EXPRIRY_IN_DAYS));
	}
	
}
