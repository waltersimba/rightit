package co.za.rightit.taxibook.spec.query;

import co.za.rightit.commons.repository.spec.query.FindByFieldValueSpec;

public class FindVerificationTokenSpec extends FindByFieldValueSpec {

	public FindVerificationTokenSpec(String token) {
		super("token", token);
	}

}
