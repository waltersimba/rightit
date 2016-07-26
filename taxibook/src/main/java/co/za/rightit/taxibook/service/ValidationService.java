package co.za.rightit.taxibook.service;

public interface ValidationService<T> {
	
	boolean validate(T obj);
	
}
