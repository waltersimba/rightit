package com.rightit.taxibook.domain;

import org.bson.types.ObjectId;

public class Identifiable {

	protected ObjectId id;

	public Identifiable() {
		this(null);
	}
	
	public Identifiable(ObjectId id) {
		this.id = id;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}