package com.rightit.taxibook.repository.spec;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import org.bson.conversions.Bson;

import com.rightit.taxibook.domain.VerificationToken.VerificationTokenType;

public class FindActiveVerificationTokenSpecification implements MongoSpecification {

	private String userId = null;
	private VerificationTokenType tokenType;
	
	public FindActiveVerificationTokenSpecification(String userId, VerificationTokenType tokenType) {
		this.userId = userId;
		this.tokenType = tokenType;
	}
	
	@Override
	public Bson toMongoQuery() {
		return and(eq("userId", userId), eq("verified", Boolean.FALSE), eq("tokenType", tokenType.toString()));
	}

}
