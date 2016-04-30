package com.rightit.taxibook.spec.query;

import org.bson.conversions.Bson;

import com.rightit.taxibook.spec.Specification;

public interface MongoQuerySpecification extends Specification {

	Bson toMongoQuery();
}
