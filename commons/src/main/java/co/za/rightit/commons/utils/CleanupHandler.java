package co.za.rightit.commons.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;

/**
 * Injectable handler that allows Guice classes to make themselves cleanup
 * capable.
 */
@Singleton
public class CleanupHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CleanupHandler.class);
	private List<CleanupCapable> cleanupHandlers = Lists.newArrayList();

	public CleanupHandler() {
	}

	/**
	 * Add a new class instance for running cleanup code.
	 * 
	 * @param cleanupCapable
	 *            class instance implementing CleanupCapable.
	 */
	public void register(CleanupCapable cleanupCapable) {
		Preconditions.checkNotNull(cleanupCapable, "cleanupCapable");
		LOGGER.debug("registering {}", cleanupCapable.getClass().getName());
		cleanupHandlers.add(cleanupCapable);
	}

	public void cleanup() {
		for (CleanupCapable cleanupHandler : cleanupHandlers) {
			LOGGER.debug("cleanup {}", cleanupHandler.getClass().getName());
			cleanupHandler.cleanup();
		}
	}
	
}
