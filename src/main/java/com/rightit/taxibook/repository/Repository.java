package com.rightit.taxibook.repository;

import com.rightit.taxibook.domain.Identifiable;
import com.rightit.taxibook.repository.specs.Specification;

public interface Repository<T extends Identifiable> {

	T findOne(Specification specification);
}