package com.rightit.taxibook.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.rightit.taxibook.validation.exception.ValidationException;

public abstract class AbstractService {
	
	private Validator validator;
	
	public AbstractService(Validator validator) {
		this.validator = validator;
	}
	
	protected void validate(Object request) {
		Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(request);
		if (constraintViolations.size() > 0) {
			throw new ValidationException(constraintViolations);
		}
	}
}
