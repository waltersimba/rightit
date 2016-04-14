package com.rightit.taxibook.repository;

import org.bson.Document;

import com.rightit.taxibook.domain.User;

public interface UserRepository extends MongoRepository<User> {
	User map(Document document);
}
