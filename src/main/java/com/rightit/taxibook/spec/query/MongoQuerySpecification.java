package com.rightit.taxibook.repository.spec;

import org.bson.conversions.Bson;

public interface MongoSpecification extends Specification {
	
	Bson toMongoQuery();
}
