package com.rightit.taxibook.spec.update;

import org.bson.types.ObjectId;

public class UpdateUserPasswordSpec extends UpdateFieldSpec {

	public UpdateUserPasswordSpec(ObjectId id, String newHashedPassword) {
		super(id, newHashedPassword);
	}

	@Override
	public String getFieldName() {
		return "hashedPassword";
	}	

}
