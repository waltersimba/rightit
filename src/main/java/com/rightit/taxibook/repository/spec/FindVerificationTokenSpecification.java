package com.rightit.taxibook.repository.spec;

public class FindVerificationTokenSpecification extends FindByFieldValueSpecification {

	public FindVerificationTokenSpecification(String token) {
		super("token", token);
	}

}
