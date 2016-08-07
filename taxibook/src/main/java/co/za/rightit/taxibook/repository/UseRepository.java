package co.za.rightit.taxibook.repository;

import javax.inject.Inject;
import javax.inject.Provider;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import co.za.rightit.commons.repository.AbstractMongoRepository;
import co.za.rightit.taxibook.domain.User;

public class UseRepository extends AbstractMongoRepository<User> {
	
	private Provider<MongoDatabase> mongoDatabaseProvider;
	
	@Inject
	public UseRepository(Provider<MongoDatabase> mongoDatabaseProvider, Provider<ObjectMapper> objectMapper) {
		super(objectMapper);
		this.mongoDatabaseProvider = mongoDatabaseProvider;
	}

	@Override
	public MongoCollection<Document> getCollection() {
		return mongoDatabaseProvider.get().getCollection("users");
	}

	@Override
	public Class<User> getResultType() {
		return User.class;
	}

}
