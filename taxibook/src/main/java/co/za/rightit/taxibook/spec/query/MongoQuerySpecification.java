package co.za.rightit.taxibook.spec.query;

import org.bson.conversions.Bson;

import co.za.rightit.taxibook.spec.Specification;

public interface MongoQuerySpecification extends Specification {

	Bson toMongoQuery();
}
