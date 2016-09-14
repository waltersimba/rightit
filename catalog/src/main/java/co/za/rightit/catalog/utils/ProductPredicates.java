package co.za.rightit.catalog.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.domain.ProductSearch;
import co.za.rightit.catalog.domain.StockStatus;

public final class ProductPredicates {

	public enum SearchQuery {
		CATEGORIES(hasTags), AVAILABILITY(availability);
		
		private Function<List<String>, Predicate<Product>> predicateFunction;
		
		SearchQuery(Function<List<String>, Predicate<Product>> predicateFunction) {
			this.predicateFunction = predicateFunction;
		}

		public static SearchQuery fromString(String value) {
			if (value != null) {
				for (SearchQuery query : SearchQuery.values()) {
					if (value.equalsIgnoreCase(query.name()))
						return query;
				}
			}
			throw new NoSuchElementException("Type not found: " + value);
		}
		
		public Function<List<String>, Predicate<Product>> getPredicateFunction() {
			return predicateFunction;
		}
	}

	public static Predicate<Product> hasPhoto() {
		return product -> product.hasPhoto();
	}
	
	public static Predicate<Product> hasTags(List<String> titles) {
		return product -> !Collections.disjoint(product.getTags(), titles);
	}
	
	public static Predicate<Product> hasTag(String title) {
		return product -> product.getTags().contains(title);
	}
	
	public static Predicate<Product> stockAvailable() {
		return product -> product.getInventory() > 0;
	}
	
	public static Predicate<Product> outOfStock() {
		return product -> product.getInventory() <= 0;
	}
		
	public static Predicate<Product> buildPredicate(ProductSearch productSearch) {
		Map<String, List<String>> searchCriteria = productSearch.getCriteria();
		Predicate<Product> predicate = null;
		for(String key : searchCriteria.keySet()) {
			SearchQuery query = SearchQuery.fromString(key);
			Function<List<String>, Predicate<Product>> predicateFunction = query.getPredicateFunction();
			List<String> values = searchCriteria.get(key);
			if(!values.isEmpty()) {
				if(predicate == null) {
					predicate = predicateFunction.apply(values);
				} else {
					predicate = predicate.or(predicateFunction.apply(values));
				}
			}			
		}
		return predicate != null ? predicate : PredicateHelper.matchAll(); 
	}

	
	private static final Function<List<String>, Predicate<Product>> hasTags = new Function<List<String>, Predicate<Product>>() {

		@Override
		public Predicate<Product> apply(List<String> tags) {
			Predicate<Product> predicate = null;
			for(String tag : tags) {
				if(predicate == null) {
					predicate = hasTag(tag);
				} else {
					predicate = predicate.or(hasTag(tag));
				}
			}
			return predicate;
		}
	};
	
	private static final Function<List<String>, Predicate<Product>> availability = new Function<List<String>, Predicate<Product>>() {

		@Override
		public Predicate<Product> apply(List<String> availability) {
			Predicate<Product> predicate = null;
			if(availability.contains(StockStatus.IN_STOCK.getTitle())) {
				predicate = stockAvailable();
			}
			if(availability.contains(StockStatus.OUT_OF_STOCK.getTitle())) {
				predicate = predicate == null ? outOfStock() : predicate.or(outOfStock());
			}
			return predicate != null ? predicate : PredicateHelper.matchNone();
		}
	};

}
