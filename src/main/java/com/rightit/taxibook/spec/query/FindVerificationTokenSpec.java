package com.rightit.taxibook.repository.spec;

public class FindVerificationTokenSpec extends FindByFieldValueSpec {

	public FindVerificationTokenSpec(String token) {
		super("token", token);
	}

}
