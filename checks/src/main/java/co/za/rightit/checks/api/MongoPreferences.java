package co.za.rightit.checks.api;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.model.Node;

public class MongoPreferences extends AbstractPreferences {

    private CheckConfig config;

    public MongoPreferences(CheckConfig config) {
        this(null, "", config);
    }

    public MongoPreferences(AbstractPreferences parent, String name, CheckConfig config) {
        super(parent, name);
        Objects.requireNonNull(config, "CheckConfig cannot be null");
        this.config = config;
    }

    @Override
    protected void putSpi(String key, String value) {
        if (parent() == null) {
            return;
        } else {
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
    	throw new UnsupportedOperationException("Node removal is not supported");
    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        throw new UnsupportedOperationException("Node removal is not supported");
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
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
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
