package com.rightit.taxibook.repository;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.repository.spec.MongoSpecification;
import com.rightit.taxibook.repository.spec.Specification;

public class UseRepositoryImpl implements UserRepository {
	
	private Logger logger = Logger.getLogger(UseRepositoryImpl.class);
	private Provider<MongoDatabase> mongoDatabaseProvider;
	private Provider<ObjectMapper> objectMapperProvider;
	
	@Inject
	public UseRepositoryImpl(Provider<MongoDatabase> mongoDatabaseProvider, Provider<ObjectMapper> objectMapperProvider) {
		this.mongoDatabaseProvider = mongoDatabaseProvider;
		this.objectMapperProvider = objectMapperProvider;
	}

	@Override
	public User findOne(Specification specification) {
		final MongoSpecification mongoSpecification = (MongoSpecification)specification;
		final Bson query = mongoSpecification.toMongoQuery();
		final Document document = getCollection().find(query).first();
		return document != null ? map(document) : null;
	}

	@Override
	public MongoCollection<Document> getCollection() {
		return mongoDatabaseProvider.get().getCollection("users");
	}
	
	private ObjectMapper getObjectMapper() {
		return objectMapperProvider.get();
	}

	@Override
	public User map(Document document) {
		User user = null;
		try {
			user = getObjectMapper().readValue(document.toJson(), User.class);
		} catch (IOException ex) {
			logger.error(ex);
		}
		return user;
	}

}
