package co.za.rightit.commons.repository.spec.query;

import org.bson.conversions.Bson;

import co.za.rightit.commons.repository.spec.Specification;

public interface MongoQuerySpecification extends Specification {

	Bson toMongoQuery();
}
