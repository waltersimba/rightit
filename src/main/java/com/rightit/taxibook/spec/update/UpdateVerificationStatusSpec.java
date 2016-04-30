package com.rightit.taxibook.spec.update;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;

public class UpdateVerificationStatusSpec implements MongoUpdateSpecification {

	private static final String DOCUMENT_KEY = "_id";
	private static final String FIELD_NAME = "verified";
	private final ObjectId id;
	private final Boolean newValue;
	
	public UpdateVerificationStatusSpec(ObjectId id, Boolean newValue) {
		this.id = id;
		this.newValue = newValue;
	}
	
	@Override
	public Bson getFilter() {
		return (BasicDBObject)new QueryBuilder().put(DOCUMENT_KEY).is(id).get();
	}

	@Override
	public Bson getValue() {
		return new UpdateQueryBuilder().set(FIELD_NAME, newValue).get();
	}

}
