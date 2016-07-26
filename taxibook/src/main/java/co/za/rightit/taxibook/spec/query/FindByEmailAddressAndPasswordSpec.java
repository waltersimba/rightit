package co.za.rightit.taxibook.spec.query;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import org.bson.conversions.Bson;

public class FindByEmailAddressAndPasswordSpec implements MongoQuerySpecification {

	private String emailAddress;
	private String hashedPassword;

	public FindByEmailAddressAndPasswordSpec(String emailAddress, String hashedPassword) {
		this.emailAddress = emailAddress;
		this.hashedPassword = hashedPassword;
	}

	@Override
	public Bson toMongoQuery() {
		return and(eq("emailAddress", emailAddress), eq("hashedPassword", hashedPassword));
	}

}
