package co.za.rightit.healthchecks.mongo.preferences;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.za.rightit.healthchecks.model.Configuration;
import co.za.rightit.healthchecks.model.Node;
import co.za.rightit.healthchecks.mongo.CheckRepository;

public class MongoPreferences extends AbstractPreferences {

    private Logger LOGGER = LoggerFactory.getLogger(MongoPreferences.class);
    private Configuration config;
    private CheckRepository store;

    public MongoPreferences(Configuration config, CheckRepository store) {
        super(null, "");
        this.config = Objects.requireNonNull(config, "Configuration cannot be null");;
        this.store = Objects.requireNonNull(store, "CheckRepository cannot be null");
    }

    public MongoPreferences(AbstractPreferences parent, String name, Configuration config) {
        super(parent, name);
        Objects.requireNonNull(config, "Configuration cannot be null");
        this.config = config;
    }

    @Override
    protected void putSpi(String key, String value) {
        if (parent() == null) {
            return;
        } else {
            LOGGER.debug("[{}] -> {}={}", name(), key, value);
            getNode().put(key, value);
        }
    }

    @Override
    protected String getSpi(String key) {
        if (parent() == null) {
            return null;
        } else {
            return String.valueOf(getNode().get(key));
        }
    }

    @Override
    protected void removeSpi(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        if (parent() == null) {
            return new String[0];
        } else {
            Set<String> keys = getNode().getProperties().keySet();
            return keys.toArray(new String[keys.size()]);
        }
    }

    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        if (parent() == null) {
            final LinkedHashSet<String> childrenNames = new LinkedHashSet<>();
            for (Node childNode : config.getNodes()) {
                childrenNames.add(childNode.getName());
            }
            return childrenNames.toArray(new String[childrenNames.size()]);
        } else {
            return new String[0];
        }
    }

    @Override
    protected AbstractPreferences childSpi(String name) {
        return new MongoPreferences(this, name, config);
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
    	flushSpi();
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
    	getStore().updateCheck(config.getName(), config.getNodes());
    }
    
    protected CheckRepository getStore() {
    	if(parent() == null) {
    		return store;
    	} else {
    		return Objects.requireNonNull(((MongoPreferences)parent()).getStore());
    	}
    }

    private Node getNode() {
        Node nodeFound = null;
        for (Node node : config.getNodes()) {
            if (node.getName().equals(name())) {
                nodeFound = node;
                break;
            }
        }
        return nodeFound;
    }

}