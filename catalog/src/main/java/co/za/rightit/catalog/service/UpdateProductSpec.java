package co.za.rightit.catalog.service;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.commons.repository.spec.update.ReplaceQuerySpec;

public class UpdateProductSpec extends ReplaceQuerySpec {

	public UpdateProductSpec(Product product) {
		super(product.getId(), product);
	}
	
}
