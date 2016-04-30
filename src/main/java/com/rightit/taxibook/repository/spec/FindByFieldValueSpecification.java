package com.rightit.taxibook.repository.spec;

import static com.mongodb.client.model.Filters.eq;

import org.bson.conversions.Bson;

public class FindByFieldValueSpecification implements MongoSpecification {
	
	private final String fieldName;
	private final Object value;

	public FindByFieldValueSpecification(String fieldName, Object value) {
		this.fieldName = fieldName;
		this.value = value;
	}
	
	@Override
	public Bson toMongoQuery() {
		return eq(fieldName, value);
	}

}
