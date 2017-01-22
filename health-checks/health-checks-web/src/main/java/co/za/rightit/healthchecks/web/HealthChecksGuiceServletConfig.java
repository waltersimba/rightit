package co.za.rightit.healthchecks.web;

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

import co.za.rightit.healthchecks.api.modules.EventModule;
import co.za.rightit.healthchecks.api.util.Closeable;
import co.za.rightit.healthchecks.api.util.Initialiseable;
import co.za.rightit.healthchecks.mongo.modules.HealthCheckModule;
import co.za.rightit.healthchecks.mongo.modules.MongoDBModule;
import co.za.rightit.healthchecks.web.modules.RestfulResourceModule;

public class HealthChecksGuiceServletConfig extends GuiceServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthChecksGuiceServletConfig.class);

	private volatile ServletContext servletContext;

	private final Supplier<Injector> injectorSupplier;

	private static Injector createInjector(ServletContext servletContext) {
		return Guice.createInjector(
				new HealthCheckModule(),
				new EventModule(),
				new MongoDBModule(),
				new RestfulResourceModule());
	}

	public HealthChecksGuiceServletConfig() {
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
		Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());
		if(injector != null) {
			for(Key<?> key : injector.getAllBindings().keySet()) {
				if(Initialiseable.class.isAssignableFrom(key.getTypeLiteral().getRawType())) {
					try {
						Initialiseable initialiseable = (Initialiseable) injector.getInstance(key);
						LOGGER.info("Initialise {}", initialiseable.getClass().getName());
						initialiseable.initialise();
					} catch (Exception ex) {
						LOGGER.error("Error initialising object", ex);
					}
				}
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		LOGGER.debug("Destroyed");
		try {
			Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());
			if(injector != null) {
				for(Key<?> key : injector.getAllBindings().keySet()) {
					if(Closeable.class.isAssignableFrom(key.getTypeLiteral().getRawType())) {
						try {
							Closeable closeable = (Closeable) injector.getInstance(key);
							LOGGER.info("Close {}", closeable.getClass().getName());
							closeable.close();
						} catch (Exception ex) {
							LOGGER.error("Error closing object", ex);
						}
					}
				}
			}
		} finally {
			super.contextDestroyed(servletContextEvent);
		}
	}

}
