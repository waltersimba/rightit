package co.za.rightit.auth.api;

import java.security.Principal;
import java.util.Optional;

import com.google.common.base.Preconditions;

public abstract class AuthFilter<C, P extends Principal> {

	protected String prefix;
	protected Authenticator<C, P> authenticator;

	protected boolean authenticate(C credentials) throws AuthenticationException {
		if(credentials == null) return false;
		final Optional<P> principal = authenticator.authenticate(credentials);
		return principal.isPresent();
	}
	
	public abstract static class AuthFilterBuilder<C, P extends Principal, T extends AuthFilter<C, P>> {

		private String prefix = "Basic";
		private Authenticator<C, P> authenticator;

		public AuthFilterBuilder<C, P, T> setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public AuthFilterBuilder<C, P, T> setAuthenticator(Authenticator<C, P> authenticator) {
			this.authenticator = authenticator;
			return this;
		}

		public T build() {
			Preconditions.checkNotNull(prefix, "Prefix is not set");
			Preconditions.checkNotNull(authenticator, "Authenticator is not set");
			final T authFilter = newInstance();
			authFilter.prefix = prefix;
			authFilter.authenticator = authenticator;
			return authFilter;
		}

		protected abstract T newInstance();
	}

}
