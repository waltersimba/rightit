package com.rightit.taxibook.domain;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Identifiable {

	protected ObjectId id;

	public Identifiable() {
	}

	public Identifiable(ObjectId id) {
		this.id = id;
	}

	@JsonProperty("_id")
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}