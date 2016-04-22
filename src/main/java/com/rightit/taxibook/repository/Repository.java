package com.rightit.taxibook.repository;

import java.util.List;
import java.util.Optional;

import com.rightit.taxibook.repository.spec.Specification;

public interface Repository<T> {

	Optional<T> findOne(Specification specification);
	
	List<T> findSome(Specification specification);	
	
	void save(T obj);
}
