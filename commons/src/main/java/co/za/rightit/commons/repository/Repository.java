package co.za.rightit.commons.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import co.za.rightit.commons.repository.spec.Specification;

public interface Repository<T> {

	CompletableFuture<Optional<T>> findOne(Specification specification);
	
	CompletableFuture<List<T>> findSome(Specification specification);
	
	CompletableFuture<Optional<T>> updateOne(Specification specification);
	
	void save(T obj);
}
