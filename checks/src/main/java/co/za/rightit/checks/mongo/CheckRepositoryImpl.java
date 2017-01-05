package co.za.rightit.checks.mongo;

import java.util.List;
import java.util.Objects;

import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.mongodb.WriteResult;

import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.model.Node;
import co.za.rightit.checks.util.Property;

public class CheckRepositoryImpl implements CheckRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckRepositoryImpl.class);
    private MongoCollection checks;

    public CheckRepositoryImpl(MongoCollection checks) {
        Objects.requireNonNull(checks, "Checks collection cannot be null.");
        this.checks = checks;
    }

    @Override
    public Optional<CheckConfig> getCheckByName(String name) {
        CheckConfig check = checks.findOne("{name: #}", name).as(CheckConfig.class);
        return Optional.fromNullable(check);
    }

    @Override
    public boolean updateCheck(String name, List<Node> nodes) {
        WriteResult updateResult = checks.update("{name: #}",name).with("{$set: {nodes: #}}", nodes);
        boolean updated = updateResult != null && updateResult.getN() == 1;
        if(updated) {
            LOGGER.debug("Updated \"{}\".", name);
        } else {
            LOGGER.warn("Failed to update \"{}\".", name);
        }
        return updated;
    }

    @Override
    public boolean updateNodeProperty(String checkName, int nodeIndex, Property<String, ?> property) {
        WriteResult updateResult = checks.update("{name:#}", checkName).with("{$set:{nodes.#.properties.#:#}}",
                nodeIndex,
                property.getKey(),
                property.getValue());
        boolean updated = updateResult != null && updateResult.getN() == 1;
        if(updated) {
            LOGGER.info("Updated {}.nodes[{}].properties.{}={}", checkName, nodeIndex, property.getKey(), property.getValue());
        } else {
            LOGGER.warn("Failed to update {}.nodes[{}].properties.{}={}", checkName, nodeIndex, property.getKey(), property.getValue());
        }
        return updated;
    }

}
