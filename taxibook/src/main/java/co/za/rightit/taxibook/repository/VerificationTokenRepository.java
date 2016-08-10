package co.za.rightit.taxibook.repository;

import javax.inject.Inject;
import javax.inject.Provider;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import co.za.rightit.commons.repository.AbstractMongoRepository;
import co.za.rightit.taxibook.domain.VerificationToken;

public class VerificationTokenRepository extends AbstractMongoRepository<VerificationToken> {

	@Override
	public MongoCollection<Document> getCollection() {
		return mongoDatabaseProvider.get().getCollection("verification_tokens");
	}

	@Override
	public Class<VerificationToken> getResultType() {
		return VerificationToken.class;
	}
}
