package com.rightit.taxibook.repository;

import javax.inject.Inject;
import javax.inject.Provider;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rightit.taxibook.domain.VerificationToken;

public class VerificationTokenRepository extends AbstractMongoRepository<VerificationToken> {
	private Provider<MongoDatabase> mongoDatabaseProvider;
	
	@Inject
	public VerificationTokenRepository(Provider<MongoDatabase> mongoDatabaseProvider, Provider<ObjectMapper> objectMapper) {
		super(objectMapper);
		this.mongoDatabaseProvider = mongoDatabaseProvider;
	}

	@Override
	public MongoCollection<Document> getCollection() {
		return mongoDatabaseProvider.get().getCollection("verification_tokens");
	}

	@Override
	public Class<VerificationToken> getResultType() {
		return VerificationToken.class;
	}
}
