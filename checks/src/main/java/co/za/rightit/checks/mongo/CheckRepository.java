package co.za.rightit.checks.mongo;

import java.util.List;

import com.google.common.base.Optional;

import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.model.Node;
import co.za.rightit.checks.util.Property;

public interface CheckRepository {
	Optional<CheckConfig> getCheckByName(String name);
	boolean updateCheck(String name, List<Node> nodes);
	boolean updateNodeProperty(String checkName, int nodeIndex, Property<String, ?> property);
}
