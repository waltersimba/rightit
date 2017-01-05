package co.za.rightit.checks.api;

import com.google.common.base.Optional;

import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.mongo.CheckRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.prefs.*;

public class MongoPreferencesFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoPreferencesFactory.class);

    public static Preferences getPreferences(String name) {
        final Optional<CheckConfig> configOptional = getCheckRepository().getCheckByName(name);
        if(!configOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("Failed to retrieve check by name: %s", name));
        }
        final PreferenceChangeListener listener = new PreferenceChangeListener() {
            @Override
            public void preferenceChange(PreferenceChangeEvent evt) {
                String nodeName = evt.getNode().name();
                LOGGER.info(String.format("Preference changed event: New value for %s is %s at node %s", evt.getKey(), evt.getNewValue(), nodeName));
                CheckConfig config = configOptional.get();
                getCheckRepository().updateCheck(config.getName(),config.getNodes());
            }
        }; 
        return new MongoPreferences(getCheckRepository().getCheckByName(name).get(), listener);
    }

    private static CheckRepository getCheckRepository() {
        return null;
    }

}

