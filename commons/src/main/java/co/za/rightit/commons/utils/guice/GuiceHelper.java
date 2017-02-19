package co.za.rightit.commons.utils.guice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 *  Helper for using guice with some basic lifecycle.
 *  
 *  Reference: https://github.com/Netflix/iep/tree/master/iep-guice 
 * */
public final class GuiceHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(GuiceHelper.class);

  private Injector injector;

  public GuiceHelper() {
  }

  /** Return the injector used with the lifecycle. */
  public Injector getInjector() {
    return injector;
  }

  /** Start up with an arbitrary list of modules. */
  public void start(Module... modules) throws Exception {
    start(Arrays.asList(modules));
  }

  /** Start up with an arbitrary list of modules. */
  public void start(Iterable<Module> modules) throws Exception {
    List<Module> ms = new ArrayList<>();
    ms.add(new LifecycleModule());
    for (Module m : modules) {
      LOGGER.debug("adding module: {}", m.getClass());
      ms.add(m);
    }
    injector = Guice.createInjector(ms);
  }

  /** Shutdown classes with {@link javax.annotation.PreDestroy}. */
  public void shutdown() throws Exception {
    PreDestroyList list = injector.getInstance(PreDestroyList.class);
    list.invokeAll();
  }

  /** Add a shutdown hook for this instance. */
  public void addShutdownHook() {
    final Runnable r = new Runnable() {
      @Override public void run() {
        try {
          shutdown();
        } catch (Exception e) {
          LOGGER.warn("exception during shutdown sequence", e);
        }
      }
    };
    Runtime.getRuntime().addShutdownHook(new Thread(r, "ShutdownHook"));
  }
}

