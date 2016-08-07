package co.za.rightit.commons.repository.spec.query;

import org.bson.types.ObjectId;

public class FindByIdSpec extends FindByFieldValueSpec {

	public FindByIdSpec(String id) {
		super("_id", new ObjectId(id));
	}
}
