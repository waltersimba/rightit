package co.za.rightit.healthchecks.api.util.guice;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import co.za.rightit.healthchecks.api.util.Closeable;

public class LifeCycleObjectRepository {
	 
    private static final Logger LOGGER = LoggerFactory.getLogger(LifeCycleObjectRepository.class);
 
    private final Set<Closeable> closeableObjects = Sets.newConcurrentHashSet();
 
    void register(Closeable closeable) {
        if(closeableObjects.add(closeable)) {
            LOGGER.info("Register {} for close at shutdown", closeable);
        }
    }
 
    public synchronized void closeAll() {
        closeableObjects.forEach(closeable -> {
            try {
                LOGGER.info("Close {}", closeable);
                closeable.close();
            } catch (Exception ex) {
                LOGGER.error("Error closing object", ex);
            }
        });
        closeableObjects.clear();
    }
    
}
