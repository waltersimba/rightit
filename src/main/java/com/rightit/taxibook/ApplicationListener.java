package com.rightit.taxibook;

import static com.google.common.base.Suppliers.memoize;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.rightit.taxibook.module.ConfigurationModule;
import com.rightit.taxibook.module.RepositoryModule;
import com.rightit.taxibook.module.ResourceModule;
import com.rightit.taxibook.module.ServiceModule;
import com.rightit.taxibook.module.ShiroSecurityModule;

public class ApplicationListener extends GuiceServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationListener.class);
	
	private volatile ServletContext servletContext;
	
	private final Supplier<Injector> injectorSupplier;

	private static Injector createInjector(ServletContext servletContext) {
		return Guice.createInjector(
				new ConfigurationModule(), 
				new RepositoryModule(), 
				new ServiceModule(),
				new ResourceModule(),
				new ShiroSecurityModule(servletContext));
	}

	public ApplicationListener() {
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
		super.contextDestroyed(servletContextEvent);
	}

}
