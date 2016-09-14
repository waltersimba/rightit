package co.za.rightit.catalog.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.base.Splitter;

public class ProductSearchCriteria implements SearchCriteria {

	private List<String> tags = new ArrayList<>();
	
	public ProductSearchCriteria withTags(String text) {
		if(text != null) {
			Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
			tags.addAll(splitter.splitToList(text));
		}
		return this;
	}
	
	public List<String> getTags() {
		return Collections.unmodifiableList(tags);
	}

	public boolean isEmpty() {
		return tags.isEmpty();
	}
	
	public Predicate<Product> anyPredicate(List<Product> products) {
		Predicate<Product> withTagsPredicate = product -> !Collections.disjoint(product.getTags(), this.getTags());
		Predicate<Product> stockAvailable = product -> product.getInventory() > 0;
		Predicate<Product> outOfStock = product -> product.getInventory() <= 0;
		return withTagsPredicate.or(stockAvailable).or(outOfStock);
	}
	
}
