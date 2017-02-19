package co.za.rightit.commons.utils.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.ProvisionListener;

/**
 * Helper for listening to injection events and invoking the PostConstruct and PreDestroy
 * annotated methods.
 */
public class LifecycleModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleModule.class);

  private static class BindingListener implements ProvisionListener {
    private PreDestroyList preDestroyList;

    BindingListener(PreDestroyList preDestroyList) {
      this.preDestroyList = preDestroyList;
    }

    @Override 
    public <T> void onProvision(ProvisionInvocation<T> provisionInvocation) {
      T value = provisionInvocation.provision();
      AnnotationUtils.invokePostConstruct(LOGGER, value, preDestroyList);
    }
  }

  @Override 
  protected void configure() {
    PreDestroyList list = new PreDestroyList();
    bindListener(Matchers.any(), new BindingListener(list));
    bind(PreDestroyList.class).toInstance(list);
  }

  @Override 
  public boolean equals(Object obj) {
    return obj != null && getClass().equals(obj.getClass());
  }

  @Override 
  public int hashCode() {
    return getClass().hashCode();
  }
}

