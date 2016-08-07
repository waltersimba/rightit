package co.za.rightit.commons.repository.spec.update;

import org.bson.conversions.Bson;

import co.za.rightit.commons.repository.spec.Specification;

public interface MongoUpdateSpecification extends Specification {

	Bson getFilter();

	Bson getValue();
}
