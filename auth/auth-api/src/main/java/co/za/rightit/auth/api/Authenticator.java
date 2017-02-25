package co.za.rightit.auth.api;

import java.util.Optional;

public interface Authenticator<C,P> {

	Optional<P> authenticate(C credentials) throws AuthenticationException;
}
