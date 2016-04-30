package com.rightit.taxibook.spec.query;

public class FindByEmailAddressSpec extends FindByFieldValueSpec {

	public FindByEmailAddressSpec(String emailAddress) {
		super("emailAddress", emailAddress);
	}

}
