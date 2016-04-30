package com.rightit.taxibook.repository.spec;

public class FindByEmailAddressSpecification extends FindByFieldValueSpecification {
	
	public FindByEmailAddressSpecification(String emailAddress) {
		super("emailAddress", emailAddress);
	}

}
