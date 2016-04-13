package com.rightit.taxibook.repository.specs;

import org.bson.conversions.Bson;

public interface MongoSpecification extends Specification {
	
	Bson toMongoQuery();
}
