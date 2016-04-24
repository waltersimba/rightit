package com.rightit.taxibook.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.rightit.taxibook.repository.spec.Specification;

public interface Repository<T> {

	CompletableFuture<Optional<T>> findOne(Specification specification);
	
	CompletableFuture<List<T>> findSome(Specification specification);	
	
	void save(T obj);
}
