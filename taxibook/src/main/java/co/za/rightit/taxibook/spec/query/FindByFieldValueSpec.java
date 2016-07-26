package co.za.rightit.taxibook.spec.query;

import static com.mongodb.client.model.Filters.eq;

import org.bson.conversions.Bson;

public class FindByFieldValueSpec implements MongoQuerySpecification {

	private final String fieldName;
	private final Object value;

	public FindByFieldValueSpec(String fieldName, Object value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	public Bson toMongoQuery() {
		return eq(fieldName, value);
	}

}
