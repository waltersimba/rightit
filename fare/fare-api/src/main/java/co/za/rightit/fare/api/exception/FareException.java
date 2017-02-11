package co.za.rightit.fare.api.exception;

public class FareException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public FareException(String message, Throwable cause) {
		super(message, cause);
	}

}
