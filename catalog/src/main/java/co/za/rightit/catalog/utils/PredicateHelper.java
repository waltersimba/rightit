package co.za.rightit.catalog.utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateHelper {

	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		return list.stream().filter(predicate).collect(Collectors.<T>toList());
	}
	
	public static <T> List<T> parallelFilter(List<T> list, Predicate<T> predicate) {
		return list.parallelStream().filter(predicate).collect(Collectors.<T>toList());
	}
	
	public static <T> Predicate<T> matchAll() {
		return new Predicate<T>() {

			@Override
			public boolean test(T value) {
				return true;
			}				
		};
	}

	public static <T> Predicate<T> matchNone() {
		return new Predicate<T>() {

			@Override
			public boolean test(T value) {
				return false;
			}				
		};
	}

}
