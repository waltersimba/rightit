package co.za.rightit.taxibook.spec.update;

import org.bson.types.ObjectId;

import co.za.rightit.commons.repository.spec.update.UpdateFieldSpec;

public class UpdateUserPasswordSpec extends UpdateFieldSpec {

	public UpdateUserPasswordSpec(ObjectId id, String newHashedPassword) {
		super(id, newHashedPassword);
	}

	@Override
	public String getFieldName() {
		return "hashedPassword";
	}	

}
