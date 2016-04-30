package com.rightit.taxibook.repository.spec;

public class FindByEmailAddressSpec extends FindByFieldValueSpec {
	
	public FindByEmailAddressSpec(String emailAddress) {
		super("emailAddress", emailAddress);
	}

}
