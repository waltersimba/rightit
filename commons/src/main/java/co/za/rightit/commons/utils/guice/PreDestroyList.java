package co.za.rightit.commons.utils.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Objects with a PreDestroy annotation that need to be cleaned up with the application.
 */
@Singleton
public class PreDestroyList {

  private static final Logger LOGGER = LoggerFactory.getLogger(PreDestroyList.class);

  private final CopyOnWriteArrayList<Object> cleanupList;

  PreDestroyList() {
    this.cleanupList = new CopyOnWriteArrayList<>();
  }

  void add(Object obj) {
    LOGGER.debug("registering class with cleanup manager {}", obj.getClass().getName());
    cleanupList.add(obj);
  }

  public void invokeAll() throws Exception {
    int n = cleanupList.size();
    for (int i = n - 1; i >= 0; --i) {
      Object obj = cleanupList.get(i);
      Method preDestroy = AnnotationUtils.getPreDestroy(obj.getClass());
      if (preDestroy != null) {
        LOGGER.debug("invoking @PreDestroy for {}", obj.getClass().getName());
        preDestroy.setAccessible(true);
        preDestroy.invoke(obj);
      }
    }
  }
}
