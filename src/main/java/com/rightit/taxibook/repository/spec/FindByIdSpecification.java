package com.rightit.taxibook.repository.spec;

import static com.mongodb.client.model.Filters.eq;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class FindByIdSpecification implements MongoSpecification {
	
	private String id = null;
	
	public FindByIdSpecification(String id) {
		this.id = id;
	}

	@Override
	public Bson toMongoQuery() {
		return eq("_id", new ObjectId(id));
	}
}
