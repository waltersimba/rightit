package co.za.rightit.checks.api;

import java.util.List;

import org.jongo.MongoCollection;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.mongodb.WriteResult;

import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.model.CheckNodeConfig;

public class CheckRepositoryImpl implements CheckRepository {

	private MongoCollection checks;
	
	public CheckRepositoryImpl(MongoCollection checks) {
		Preconditions.checkNotNull(checks, "Checks collection cannot be null.");
		this.checks = checks;
	}
	
	@Override
	public Optional<CheckConfig> getCheckByName(String name) {
		CheckConfig check = checks.findOne("{name: #}", name).as(CheckConfig.class);
		return Optional.fromNullable(check);
	}

	@Override
	public boolean updateCheck(String name, List<CheckNodeConfig> nodes) {
		WriteResult updateResult = checks.update("{name: #}",name).with("{$set: {nodes: #}}", nodes);
		return updateResult.getN() == 1;
	}

}
