package com.rightit.taxibook.repository.spec;

import org.bson.types.ObjectId;

public class FindByIdSpec extends FindByFieldValueSpec {
	
	public FindByIdSpec(String id) {
		super("_id", new ObjectId(id));
	}
}
