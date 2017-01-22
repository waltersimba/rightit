package co.za.rightit.healthchecks.api.util.guice;

import java.lang.annotation.Annotation;

import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.ProvisionListener;

import co.za.rightit.healthchecks.api.util.Closeable;

public class CloseableListener implements ProvisionListener {
	 
    private final LifeCycleObjectRepository repo;
 
    public CloseableListener(LifeCycleObjectRepository repo) {
        this.repo = repo;
    }
 
    @Override
    public <T> void onProvision(ProvisionInvocation<T> provisionInvocation) {
        T provision = provisionInvocation.provision();
        if(provision instanceof Closeable && shouldManage(provisionInvocation)) {
            repo.register((Closeable)provision);
        }
    }
 
    private boolean shouldManage(ProvisionInvocation<?> provisionInvocation) {
        return provisionInvocation.getBinding().acceptScopingVisitor(new BindingScopingVisitor<Boolean>() {
            @Override
            public Boolean visitEagerSingleton() {
                return true;
            }
 
            @Override
            public Boolean visitScope(Scope scope) {
                return scope == Scopes.SINGLETON;
            }
 
            @Override
            public Boolean visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
                return scopeAnnotation.isAssignableFrom(Singleton.class);
            }
 
            @Override
            public Boolean visitNoScoping() {
                return false;
            }
        });
    }
    
}
