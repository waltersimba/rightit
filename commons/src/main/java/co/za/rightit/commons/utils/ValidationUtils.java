package co.za.rightit.commons.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import co.za.rightit.commons.exceptions.ValidationException;

public final class ValidationUtils {

	private ValidationUtils() {
		throw new AssertionError("Can't be instantiated: " + ValidationUtils.class.getSimpleName());
	}
	
	public static void validate(Object request, Validator validator) {
		Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(request);
		if (constraintViolations.size() > 0) {
			throw new ValidationException(constraintViolations);
		}
	}
}
