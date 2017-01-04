package co.za.rightit.checks.api;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

public class MongoPreferenceChangeListener implements PreferenceChangeListener {

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        String nodeName = evt.getNode().name();
        System.out.println(String.format("Preference changed event: New value for %s is %s at node %s", evt.getKey(), evt.getNewValue(), nodeName));
        //CheckConfig config = configOptional.get();
        //repository.updateCheck(config.getName(),config.getNodes());
    }
}
