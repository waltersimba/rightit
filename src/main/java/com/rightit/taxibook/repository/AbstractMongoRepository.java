package com.rightit.taxibook.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Provider;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.rightit.taxibook.repository.spec.MongoSpecification;
import com.rightit.taxibook.repository.spec.Specification;

public abstract class AbstractMongoRepository<T> implements MongoRepository<T> {

	abstract public MongoCollection<Document> getCollection();

	abstract public Class<T> getResultType();

	protected Logger logger;

	private Provider<ObjectMapper> objectMapper;

	public AbstractMongoRepository(Provider<ObjectMapper> objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Optional<T> findOne(Specification specification) {
		final MongoSpecification mongoSpecification = (MongoSpecification) specification;
		final Bson query = mongoSpecification.toMongoQuery();
		final Document document = getCollection().find(query).first();
		return document == null ? Optional.empty() : Optional.of(map(document));
	}

	@Override
	public List<T> findSome(Specification specification) {
		final MongoSpecification mongoSpecification = (MongoSpecification) specification;
		final Bson query = mongoSpecification.toMongoQuery();
		final MongoCursor<Document> cursor = getCollection().find(query).iterator();
		final List<T> items = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				items.add(map(cursor.next()));
			}
		} finally {
			cursor.close();
		}
		return items;
	}

	@Override
	public void save(T obj) {
		try {
			Document document = Document.parse(getObjectMapper().writeValueAsString(obj));
			getCollection().insertOne(document);
		} catch (JsonProcessingException ex) {
			getLogger().error(ex);
			throw new RuntimeException(ex);
		} catch (MongoException ex) {
			getLogger().error(ex);
			throw new RuntimeException(ex);
		}
	}

	public T map(Document document) {
		T obj = null;
		try {
			obj = getObjectMapper().readValue(document.toJson(), getResultType());
		} catch (IOException ex) {
			getLogger().error(ex);
			throw new RuntimeException(ex);
		}
		return obj;
	}

	protected ObjectMapper getObjectMapper() {
		return objectMapper.get();
	}

	private Logger getLogger() {
		if (null == logger) {
			logger = Logger.getLogger(getClass());
		}
		return logger;
	}
}