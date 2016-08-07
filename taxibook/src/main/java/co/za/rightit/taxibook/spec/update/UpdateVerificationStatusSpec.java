package co.za.rightit.taxibook.spec.update;

import org.bson.types.ObjectId;

import co.za.rightit.commons.repository.spec.update.UpdateFieldSpec;

public class UpdateVerificationStatusSpec extends UpdateFieldSpec {

	public UpdateVerificationStatusSpec(ObjectId id, Boolean newValue) {
		super(id, newValue);
	}

	@Override
	public String getFieldName() {
		return "verified";
	}	

}
