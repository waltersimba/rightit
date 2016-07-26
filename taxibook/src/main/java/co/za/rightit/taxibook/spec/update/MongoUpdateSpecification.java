package co.za.rightit.taxibook.spec.update;

import org.bson.conversions.Bson;

import co.za.rightit.taxibook.spec.Specification;

public interface MongoUpdateSpecification extends Specification {

	Bson getFilter();

	Bson getValue();
}
