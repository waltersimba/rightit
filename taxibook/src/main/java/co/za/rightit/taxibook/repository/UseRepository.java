package co.za.rightit.taxibook.repository;

import javax.inject.Provider;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import co.za.rightit.commons.repository.AbstractMongoRepository;
import co.za.rightit.taxibook.domain.User;

public class UseRepository extends AbstractMongoRepository<User> {
	
	private Provider<MongoDatabase> mongoDatabaseProvider;
	
	@Override
	public MongoCollection<Document> getCollection() {
		return mongoDatabaseProvider.get().getCollection("users");
	}

	@Override
	public Class<User> getResultType() {
		return User.class;
	}

}
