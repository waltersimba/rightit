package co.za.rightit.catalog.service;

import org.bson.types.ObjectId;

import co.za.rightit.commons.repository.spec.update.UpdateFieldSpec;

public class UpdateProductPhotoIdSpec extends UpdateFieldSpec {

	public UpdateProductPhotoIdSpec(String productId, Object newValue) {
		super(new ObjectId(productId), newValue);
	}

	@Override
	public String getFieldName() {
		return "photoId";
	}

}
