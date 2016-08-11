package co.za.rightit.commons.repository;

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
import com.google.inject.Inject;
import com.mongodb.Function;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import co.za.rightit.commons.repository.spec.Specification;
import co.za.rightit.commons.repository.spec.query.MongoQuerySpecification;
import co.za.rightit.commons.repository.spec.update.MongoReplaceSpecification;
import co.za.rightit.commons.repository.spec.update.MongoUpdateSpecification;

public abstract class AbstractMongoRepository<T> implements MongoRepository<T> {

	abstract public MongoCollection<Document> getCollection();

	abstract public Class<T> getResultType();

	@Inject
	protected Provider<MongoDatabase> mongoDatabaseProvider;
	@Inject
	protected Provider<ObjectMapper> objectMapper;

	@Override
	public CompletableFuture<Optional<T>> findOne(Specification specification) {
		CompletableFuture<Optional<T>> future = new CompletableFuture<>();
		try {
			MongoQuerySpecification mongoSpecification = (MongoQuerySpecification) specification;
			Bson query = mongoSpecification.toMongoQuery();
			Document document = getCollection().find(query).first();
			future.complete(Optional.ofNullable(mapperFunction.apply((document))));
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
			MongoQuerySpecification mongoSpecification = (MongoQuerySpecification) specification;
			Bson query = mongoSpecification.toMongoQuery();
			List<T> items = new ArrayList<>();
			try (MongoCursor<Document> cursor = getCollection().find(query).iterator()) {
				while (cursor.hasNext()) {
					items.add(mapperFunction.apply(cursor.next()));
				}
				future.complete(items);
			}
		} catch (Exception ex) {
			CompletableFuture<List<T>> failedFuture = new CompletableFuture<>();
			failedFuture.completeExceptionally(ex);
			return failedFuture;
		}
		return future;
	}

	@Override
	public List<T> findAll() {
		final List<T> items = new ArrayList<>();
		try (MongoCursor<Document> cursor = getCollection().find().iterator()) {
			while (cursor.hasNext()) {
				items.add(mapperFunction.apply(cursor.next()));
			}
		}
		return items;
	}

	@Override
	public void save(T obj) {
		try {
			System.out.println("Saving document: " + getObjectMapper().writeValueAsString(obj));
			Document document = Document.parse(getObjectMapper().writeValueAsString(obj));
			getCollection().insertOne(document);
		} catch (JsonProcessingException ex) {
			throw new RuntimeException(ex);
		} catch (MongoException ex) {
			throw new RuntimeException(ex);
		}
	}

	public CompletableFuture<Boolean> updateOne(final Specification specification) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();
		try {
			MongoUpdateSpecification updateSpecification = (MongoUpdateSpecification) specification;
			Document document = getCollection().findOneAndUpdate(updateSpecification.getFilter(),
					updateSpecification.getValue());
			future.complete(document != null);
		} catch (Exception ex) {
			CompletableFuture<Boolean> failedFuture = new CompletableFuture<>();
			failedFuture.completeExceptionally(ex);
			return failedFuture;
		}
		return future;
	}

	@Override
	public CompletableFuture<Boolean> replaceOne(Specification specification) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();
		try {
			MongoReplaceSpecification replaceSpecification = (MongoReplaceSpecification) specification;
			Document document = Document.parse(getObjectMapper().writeValueAsString(replaceSpecification.getValue()));
			UpdateResult updateResult = getCollection().replaceOne(replaceSpecification.getFilter(), document);
			future.complete(updateResult.getModifiedCount() > 0);
		} catch (Exception ex) {
			CompletableFuture<Boolean> failedFuture = new CompletableFuture<>();
			failedFuture.completeExceptionally(ex);
			return failedFuture;
		}
		return future;
	}

	protected ObjectMapper getObjectMapper() {
		return objectMapper.get();
	}

	private Function<Document, T> mapperFunction = new Function<Document, T>() {

		@Override
		public T apply(Document document) {
			try {
				return getObjectMapper().readValue(document.toJson(), getResultType());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	};

}
