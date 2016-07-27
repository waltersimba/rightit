package co.za.rightit.catalog.repository;

import java.util.List;
import java.util.Optional;

import co.za.rightit.catalog.domain.Product;

public interface ProductRepository {

	Optional<Product> get(String uid);
	
	List<Product> get();
	
	boolean update(Product product);
	
	boolean insert(Product product);
}
