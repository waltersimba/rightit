package co.za.rightit.commons.validation;

public interface ObjectValidator<T> {
	
	boolean validate(T obj);
	
}
