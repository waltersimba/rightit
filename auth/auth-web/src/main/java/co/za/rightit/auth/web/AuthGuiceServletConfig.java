package co.za.rightit.auth.web;

import static com.google.common.base.Suppliers.memoize;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.servlet.GuiceServletContextListener;

import co.za.rightit.auth.web.module.RestfulResourceModule;

public class AuthGuiceServletConfig extends GuiceServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthGuiceServletConfig.class);

	private volatile ServletContext servletContext;

	private final Supplier<Injector> injectorSupplier;

	private static Injector createInjector(ServletContext servletContext) {
		return Guice.createInjector(
				new RestfulResourceModule());
	}

	public AuthGuiceServletConfig() {
		this.injectorSupplier = memoize(new Supplier<Injector>() {

			@Override
			public Injector get() {
				return createInjector(servletContext);
			}
		});
	}

	@Override
	protected Injector getInjector() {
		return injectorSupplier.get();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		LOGGER.debug("Initialised");
		servletContext = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		LOGGER.debug("Destroyed");
		try {
			Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());
			//TODO: 
		} finally {
			super.contextDestroyed(servletContextEvent);
		}
	}

}
