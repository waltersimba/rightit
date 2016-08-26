package co.za.rightit.catalog.service;

import co.za.rightit.catalog.domain.FileInfo;
import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.domain.ProductSearchCriteria;
import co.za.rightit.commons.utils.Page;
import co.za.rightit.commons.utils.Pageable;

public interface ProductService {

	void save(ProductRequest request);
	
	Boolean update(ProductRequest request);
	
	Product findProduct(String productId);
	
	Page<Product> findAll(Pageable pageable);
	
	Page<Product> search(ProductSearchCriteria criteria, Pageable pageable);

	Boolean updateProductPhoto(String productId, FileInfo fileInfo);
}
