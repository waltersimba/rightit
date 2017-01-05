package co.za.rightit.checks.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Guice;
import com.google.inject.Injector;

import co.za.rightit.checks.api.MongoPreferences;
import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.modules.ChecksModule;
import co.za.rightit.checks.modules.MongoModule;
import co.za.rightit.checks.mongo.CheckRepository;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public final class MongoPreferencesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoPreferencesUtil.class);

    public static Preferences getPreferences(String[] args) {
        OptionParser parser = new OptionParser();
        OptionSpec<String> checkNameSpec = parser.accepts("check").withRequiredArg().ofType(String.class).required();
        OptionSet options = parser.parse(args);
        String checkName = checkNameSpec.value(options);
        return getPreferences(checkName, getCheckRepository());
    }

    public static Preferences getPreferences(String name, final CheckRepository repository) {
        final Optional<CheckConfig> configOptional = repository.getCheckByName(name);
        if(!configOptional.isPresent()) {
            LOGGER.error("No check found: {}!", name);
            throw new IllegalArgumentException(String.format("Failed to retrieve check by name: %s", name));
        }
        return new MongoPreferences(configOptional.get(), repository);
    }

    public static CheckRepository getCheckRepository() {
        Injector injector = Guice.createInjector(
                new MongoModule(),
                new ChecksModule()
        );
        return injector.getInstance(CheckRepository.class);
    }

    //java -jar target/checks-1.0.jar --check="MemcachedCheck"
    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("Usage: --check=<check name>\n");
            return;
        }
        try {
            Preferences prefs = MongoPreferencesUtil.getPreferences(args);
            String[] childrenNames = prefs.childrenNames();
            if(childrenNames.length <= 0) {
            	System.err.println("No children nodes found!");
            }
            for (int i=0; i<childrenNames.length; i++) {
                System.out.println("[" + childrenNames[i] + "]");
                Preferences node = prefs.node(childrenNames[i]);
                if(node.keys().length > 0) {
                    for(String key : node.keys()) {
                        System.out.println(key + "=" + node.get(key, ""));
                    }
                } else {
                    System.err.println("No keys found for node: " + node.name());
                }
            }
            Preferences node = prefs.node("hostname");
            if(node != null) {
                node.putLong("lastNotified", System.currentTimeMillis());
                node.flush();
            }
        } catch (BackingStoreException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
