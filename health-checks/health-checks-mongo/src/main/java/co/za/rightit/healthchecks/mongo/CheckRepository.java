package co.za.rightit.healthchecks.mongo;

import java.util.List;

import com.google.common.base.Optional;

import co.za.rightit.healthchecks.model.Configuration;
import co.za.rightit.healthchecks.model.Node;
import co.za.rightit.healthchecks.model.util.Property;

public interface CheckRepository {
	
	Optional<Configuration> getCheckByName(String name);
	
	boolean updateCheck(String name, List<Node> nodes);
	
	boolean updateNodeProperty(String checkName, int nodeIndex, Property<String, ?> property);
	
}
