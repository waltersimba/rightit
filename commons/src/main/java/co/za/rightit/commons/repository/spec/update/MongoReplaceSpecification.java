package co.za.rightit.commons.repository.spec.update;

import org.bson.conversions.Bson;

import co.za.rightit.commons.repository.spec.Specification;

public interface MongoReplaceSpecification extends Specification {

	Bson getFilter();

	Object getValue();
	
}