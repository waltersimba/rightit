package co.za.rightit.taxibook.spec.query;

import co.za.rightit.commons.repository.spec.query.FindByFieldValueSpec;

public class FindByEmailAddressSpec extends FindByFieldValueSpec {

	public FindByEmailAddressSpec(String emailAddress) {
		super("emailAddress", emailAddress);
	}

}
