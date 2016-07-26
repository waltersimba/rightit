package co.za.rightit.taxibook.security.shiro;

import javax.inject.Singleton;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import co.za.rightit.taxibook.security.JWTPrincipal;

@Singleton
public class JWTRealm extends AuthorizingRealm {

	@Override
	public boolean supports(AuthenticationToken token) {
		return token != null && token instanceof JWTAuthenticationToken;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		final JWTPrincipal principal = principals.oneByType(JWTPrincipal.class);
		SimpleAuthorizationInfo simpleAuthorizationInfo = null;
		if(principal != null) {
			simpleAuthorizationInfo = new SimpleAuthorizationInfo(principal.getRoles());
		}
		return simpleAuthorizationInfo;
	}

}
