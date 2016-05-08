package com.rightit.taxibook.spec.update;

import org.bson.types.ObjectId;

public class UpdateVerificationStatusSpec extends UpdateFieldSpec {

	public UpdateVerificationStatusSpec(ObjectId id, Boolean newValue) {
		super(id, newValue);
	}

	@Override
	public String getFieldName() {
		return "verified";
	}	

}
