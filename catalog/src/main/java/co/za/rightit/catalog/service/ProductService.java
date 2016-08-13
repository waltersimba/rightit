package co.za.rightit.catalog.service;

import java.util.List;

import co.za.rightit.catalog.domain.FileInfo;
import co.za.rightit.catalog.domain.Product;

public interface ProductService {

	void save(ProductRequest request);
	
	Boolean update(ProductRequest request);
	
	Product findProduct(String productId);
	
	List<Product> findAll();

	Boolean updateProductPhoto(String productId, FileInfo fileInfo);
}
