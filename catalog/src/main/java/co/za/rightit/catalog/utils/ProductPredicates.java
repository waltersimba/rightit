package co.za.rightit.catalog.utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import co.za.rightit.catalog.domain.Product;

public final class ProductPredicates {

	public static Predicate<Product> hasPhoto() {
		return product -> product.hasPhoto();
	}
	
	public static List<Product> filterProducts(List<Product> products, Predicate<Product> predicate) {
		return products.stream().filter(predicate).collect(Collectors.<Product>toList());
	}
}
