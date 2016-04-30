package com.rightit.taxibook.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Provider;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.rightit.taxibook.spec.Specification;
import com.rightit.taxibook.spec.query.MongoQuerySpecification;
import com.rightit.taxibook.spec.update.MongoUpdateSpecification;

public abstract class AbstractMongoRepository<T> implements MongoRepository<T> {

	abstract public MongoCollection<Document> getCollection();

	abstract public Class<T> getResultType();

	private Provider<ObjectMapper> objectMapper;

	public AbstractMongoRepository(Provider<ObjectMapper> objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public CompletableFuture<Optional<T>> findOne(Specification specification) {
		CompletableFuture<Optional<T>> future = new CompletableFuture<>();
		try {
			final MongoQuerySpecification mongoSpecification = (MongoQuerySpecification) specification;
			final Bson query = mongoSpecification.toMongoQuery();
			final Document document = getCollection().find(query).first();
			future.complete(document == null ? Optional.empty() : Optional.of(map(document)));
		} catch (Exception ex) {
			CompletableFuture<Optional<T>> failedFuture = new CompletableFuture<>();
			failedFuture.completeExceptionally(ex);
			return failedFuture;
		}

		return future;
	}

	@Override
	public CompletableFuture<List<T>> findSome(Specification specification) {
		CompletableFuture<List<T>> future = new CompletableFuture<>();
		try {
			final MongoQuerySpecification mongoSpecification = (MongoQuerySpecification) specification;
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
			future.complete(items);
		} catch(Exception ex) {
			CompletableFuture<List<T>> failedFuture = new CompletableFuture<>();
			failedFuture.completeExceptionally(ex);
			return failedFuture;
		}		
		return future;
	}

	@Override
	public void save(T obj) {
		try {
			Document document = Document.parse(getObjectMapper().writeValueAsString(obj));
			getCollection().insertOne(document);
		} catch (JsonProcessingException ex) {
			throw new RuntimeException(ex);
		} catch (MongoException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public CompletableFuture<Optional<T>> updateOne(final Specification specification) {
		CompletableFuture<Optional<T>> future = new CompletableFuture<>();
		try {
			final MongoUpdateSpecification updateSpecification = (MongoUpdateSpecification) specification;
			final Document document = getCollection().findOneAndUpdate(updateSpecification.getFilter(), updateSpecification.getValue());
			future.complete(document == null ? Optional.empty() : Optional.of(map(document)));
		} catch (Exception ex) {
			CompletableFuture<Optional<T>> failedFuture = new CompletableFuture<>();
			failedFuture.completeExceptionally(ex);
			return failedFuture;
		}
		return future;
	}

	public T map(Document document) {
		T obj = null;
		try {
			obj = getObjectMapper().readValue(document.toJson(), getResultType());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return obj;
	}

	protected ObjectMapper getObjectMapper() {
		return objectMapper.get();
	}

}
