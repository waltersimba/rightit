package co.za.rightit.taxibook.spec.query;

public class FindVerificationTokenSpec extends FindByFieldValueSpec {

	public FindVerificationTokenSpec(String token) {
		super("token", token);
	}

}
