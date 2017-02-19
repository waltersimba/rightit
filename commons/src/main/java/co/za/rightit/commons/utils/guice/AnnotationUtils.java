package co.za.rightit.commons.utils.guice;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Helper functions for checking the annotations on an object.
 */
class AnnotationUtils {

  private AnnotationUtils() {
  }

  static boolean hasLifecycleAnnotations(Class<?> cls) throws Exception {
    return getPostConstruct(cls) != null || getPreDestroy(cls) != null;
  }

  static Method getPostConstruct(Class<?> cls) throws Exception {
    return getAnnotatedMethod(cls, PostConstruct.class);
  }

  static Method getPreDestroy(Class<?> cls) throws Exception {
    return getAnnotatedMethod(cls, PreDestroy.class);
  }

  static Method getAnnotatedMethod(Class<?> cls, Class<? extends Annotation> anno)
      throws Exception {
    for (Method m : cls.getDeclaredMethods()) {
      if (m.getAnnotation(anno) != null) {
        return m;
      }
    }
    Class<?> superCls = cls.getSuperclass();
    return (superCls != null) ? getAnnotatedMethod(superCls, anno) : null;
  }

  static void invokePostConstruct(Logger logger, Object injectee, PreDestroyList preDestroyList) {
    try {
      Method postConstruct = AnnotationUtils.getPostConstruct(injectee.getClass());
      if (postConstruct != null) {
        logger.debug("invoking @PostConstruct for {}", injectee.getClass().getName());
        try {
          postConstruct.setAccessible(true);
          postConstruct.invoke(injectee);
        } catch (Throwable t) {
          logger.debug("error calling @PostConstruct (" + postConstruct + ")", t);
          throw t;
        }
        logger.debug("completed @PostConstruct ({})", postConstruct);
      }

      Method preDestroy = AnnotationUtils.getPreDestroy(injectee.getClass());
      if (preDestroy != null) {
        preDestroyList.add(injectee);
      }
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}

