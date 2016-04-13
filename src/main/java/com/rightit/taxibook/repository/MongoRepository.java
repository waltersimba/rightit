package com.rightit.taxibook.repository;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.rightit.taxibook.domain.Identifiable;

public interface MongoRepository<T extends Identifiable> extends Repository<T> {
	
	MongoCollection<Document> getCollection();
	
	T map(Document document);
}
