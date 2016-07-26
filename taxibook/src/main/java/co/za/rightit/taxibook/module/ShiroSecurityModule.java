package co.za.rightit.taxibook.module;

import javax.servlet.ServletContext;

import org.apache.shiro.guice.web.ShiroWebModule;

import com.google.inject.Key;

import co.za.rightit.taxibook.security.shiro.JWTRealm;
import co.za.rightit.taxibook.security.shiro.JWTTokenFilter;

public class ShiroSecurityModule extends ShiroWebModule {
	public static Key<JWTTokenFilter> JWT_TOKEN_FILTER = Key.get(JWTTokenFilter.class);

	public ShiroSecurityModule(ServletContext servletContext) {
		super(servletContext);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configureShiroWeb() {
		bindRealm().to(JWTRealm.class);	
		
		addFilterChain("/api/password/**", NO_SESSION_CREATION, ANON);
		addFilterChain("/api/users/**", NO_SESSION_CREATION, ANON);
		addFilterChain("/api/lang/**", NO_SESSION_CREATION, ANON);
		addFilterChain("/api/session/**", NO_SESSION_CREATION, ANON);
		addFilterChain("/api/logout", LOGOUT);
		addFilterChain("/api/**",NO_SESSION_CREATION, JWT_TOKEN_FILTER);
	}

}
