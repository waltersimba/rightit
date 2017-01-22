package co.za.rightit.healthchecks.api.util.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

public class LifeCycleAwareModule extends AbstractModule {
    
	private final Module module;
    private final LifeCycleObjectRepository repo;
    
    protected LifeCycleAwareModule(LifeCycleObjectRepository repo, Module module) {
        this.repo = repo;
        this.module = module;
    }
 
    @Override
    protected void configure() {
        bindListener(Matchers.any(), new CloseableListener(repo));
        install(module);
    }
    
}
