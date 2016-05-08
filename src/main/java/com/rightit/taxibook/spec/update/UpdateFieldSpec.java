package com.rightit.taxibook.spec.update;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;

public abstract class UpdateFieldSpec implements MongoUpdateSpecification {

	private static final String DOCUMENT_KEY = "_id";
	private final ObjectId id;
	private final Object newValue;
	
	public abstract String getFieldName();
	
	public UpdateFieldSpec(ObjectId id, Object newValue) {
		this.id = id;
		this.newValue = newValue;
	}
	
	@Override
	public Bson getFilter() {
		return (BasicDBObject)new QueryBuilder().put(DOCUMENT_KEY).is(id).get();
	}

	@Override
	public Bson getValue() {
		return new UpdateQueryBuilder().set(getFieldName(), newValue).get();
	} 

}
