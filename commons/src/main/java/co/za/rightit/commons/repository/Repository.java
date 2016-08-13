package co.za.rightit.commons.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import co.za.rightit.commons.repository.spec.Specification;

public interface Repository<T> {

	Optional<T> findOne(Specification specification);
	
	CompletableFuture<List<T>> findSome(Specification specification);
	
	List<T> findAll();
	
	Boolean updateOne(Specification specification);
	
	Boolean replaceOne(Specification specification);
	
	void save(T obj);
}
