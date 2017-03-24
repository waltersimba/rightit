package co.za.rightit.messaging.web.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import co.za.rightit.commons.utils.CleanupCapable;
import co.za.rightit.commons.utils.CleanupHandler;
import co.za.rightit.commons.utils.guice.TypeMatchers;

public class CleanupModule extends AbstractModule {

    private CleanupHandler cleanupHandler = new CleanupHandler();

    @Override
    protected void configure() {
        bind(CleanupHandler.class).toInstance(cleanupHandler);
        bindListener(TypeMatchers.subclassesOf(CleanupCapable.class), new CleanupCapableListener());
    }

    private class CleanupCapableListener implements TypeListener {

        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> typeEncounter) {
            typeEncounter.register(new InjectionListener<I>() {
                @Override
                public void afterInjection(I instance) {
                    if(instance instanceof CleanupCapable) {
                        cleanupHandler.register((CleanupCapable) instance);
                    }
                }
            });
        }
    }

}
