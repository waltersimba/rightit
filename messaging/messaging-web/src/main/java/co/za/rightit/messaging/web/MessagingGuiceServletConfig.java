package co.za.rightit.messaging.web;

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

import co.za.rightit.commons.event.EventModule;
import co.za.rightit.commons.event.EventService;
import co.za.rightit.commons.event.EventSubscriber;
import co.za.rightit.commons.utils.CleanupHandler;
import co.za.rightit.commons.utils.guice.LifecycleModule;
import co.za.rightit.messaging.email.EmailModule;
import co.za.rightit.messaging.web.modules.RestfulResourceModule;

public class MessagingGuiceServletConfig extends GuiceServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingGuiceServletConfig.class);

	private volatile ServletContext servletContext;

	private final Supplier<Injector> injectorSupplier;

	private static Injector createInjector(ServletContext servletContext) {
		return Guice.createInjector(
				new LifecycleModule(),
				new EmailModule(),
				new EventModule(),
				new RestfulResourceModule());
	}

	public MessagingGuiceServletConfig() {
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
		servletContext = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
		LOGGER.debug("Initialised");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		try {
			final Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());
			final EventService eventService = injector.getInstance(Key.get(EventService.class));
			if(injector != null) {
				for(Key<?> key : injector.getAllBindings().keySet()) {
					if(EventSubscriber.class.isAssignableFrom(key.getTypeLiteral().getRawType())) {
						EventSubscriber eventSubscriber = null;
						try {
							eventSubscriber = (EventSubscriber) injector.getInstance(key);
							eventService.unregister(eventSubscriber);
						} catch (Exception ex) {
							LOGGER.error("Error unregistering event subscriber: " + eventSubscriber.getSubscriberId(), ex);
						}
					}
				}
			}
			final CleanupHandler cleanups = injector.getInstance(CleanupHandler.class);
	        cleanups.cleanup();
		} finally {
			super.contextDestroyed(servletContextEvent);
			LOGGER.debug("Destroyed");
		}
	}
}
