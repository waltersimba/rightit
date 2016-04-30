package com.rightit.taxibook.spec.update;

import org.bson.conversions.Bson;

import com.rightit.taxibook.spec.Specification;

public interface MongoUpdateSpecification extends Specification {

	Bson getFilter();

	Bson getValue();
}
