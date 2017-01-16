package co.za.rightit.healthchecks.api.security;

import java.util.Optional;

public interface Authenticator<C,P> {

	Optional<P> authenticate(C credentials) throws AuthenticationException;
}
