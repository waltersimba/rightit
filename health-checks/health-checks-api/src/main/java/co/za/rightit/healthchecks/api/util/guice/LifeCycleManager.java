package co.za.rightit.healthchecks.api.util.guice;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class LifeCycleManager {
	 
    private final LifeCycleObjectRepository repo = new LifeCycleObjectRepository();
    private final Injector injector;
 
    public LifeCycleManager(Module... modules) {
        this(ImmutableList.copyOf(modules));
    }
 
    public LifeCycleManager(Iterable<Module> modules) {
        this.injector = Guice.createInjector(enableLifeCycleManagement(repo, modules));
        addShutdownHook();
    }
 
    public <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }
 
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(repo::closeAll));
    }
 
    private static Iterable<Module> enableLifeCycleManagement(LifeCycleObjectRepository repo, Iterable<Module> modules) {
        return StreamSupport.stream(modules.spliterator(), false)
                .map(m -> new LifeCycleAwareModule(repo, m))
                .collect(Collectors.toList());
    }
    
}

