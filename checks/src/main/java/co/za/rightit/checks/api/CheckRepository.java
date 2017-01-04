package co.za.rightit.checks.api;

import java.util.List;

import com.google.common.base.Optional;

import co.za.rightit.checks.model.CheckConfig;
import co.za.rightit.checks.model.CheckNodeConfig;

public interface CheckRepository {
	Optional<CheckConfig> getCheckByName(String name);
	boolean updateCheck(String name, List<CheckNodeConfig> nodes);
}
