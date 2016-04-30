package com.rightit.taxibook.repository.spec;

import org.bson.types.ObjectId;

public class FindByIdSpecification extends FindByFieldValueSpecification {
	
	public FindByIdSpecification(String id) {
		super("_id", new ObjectId(id));
	}
}
