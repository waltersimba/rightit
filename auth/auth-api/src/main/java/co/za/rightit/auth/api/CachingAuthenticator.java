package co.za.rightit.auth.api;

import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

public class CachingAuthenticator<C, P extends Principal> implements Authenticator<C, P> {

	private final LoadingCache<C, Optional<P>> cache;
	
	public CachingAuthenticator(final Authenticator<C, P> authenticator, CacheBuilderSpec spec) {
		this(authenticator, CacheBuilder.from(spec));
	}
	
	public CachingAuthenticator(final Authenticator<C, P> authenticator, CacheBuilder<Object, Object> builder) {
		this.cache = builder.build(new CacheLoader<C, Optional<P>>() {

			@Override
			public Optional<P> load(C key) throws Exception {
				final Optional<P> principal = authenticator.authenticate(key);
                if (!principal.isPresent()) {
                    throw new InvalidCredentialsException();
                }
                return principal;
			}
		});
	}
	
	@Override
	public Optional<P> authenticate(C credentials) throws AuthenticationException {
		try {
            return cache.get(credentials);
        } catch (ExecutionException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof InvalidCredentialsException) {
                return Optional.empty();
            }
            // Attempt to re-throw as-is
            Throwables.propagateIfPossible(cause, AuthenticationException.class);
            throw new AuthenticationException(cause);
        } catch (UncheckedExecutionException ex) {
            Throwables.propagateIfPossible(ex);
            throw ex;
        } 
	}
	
	public void invalide(C credentials) {
		cache.invalidate(credentials);
	}
	
	/**
	 * Used to prevent caching of invalid credentials.
	 */
	private static class InvalidCredentialsException extends Exception {
		private static final long serialVersionUID = 1L;
	}

}
