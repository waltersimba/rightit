package co.za.rightit.catalog.service;

import org.bson.types.ObjectId;

import co.za.rightit.commons.repository.spec.update.UpdateFieldSpec;

public class UpdateProductPhotoIdSpec extends UpdateFieldSpec {

	public UpdateProductPhotoIdSpec(ObjectId productId, Object newValue) {
		super(productId, newValue);
	}

	@Override
	public String getFieldName() {
		return "photoId";
	}

}
