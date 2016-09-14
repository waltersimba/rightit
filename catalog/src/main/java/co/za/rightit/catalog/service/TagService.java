package co.za.rightit.catalog.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import co.za.rightit.catalog.domain.StockStatus;
import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.domain.Tag;
import co.za.rightit.catalog.utils.ProductPredicates;

public class TagService {

	@Inject
	private ProductsCache productsCache;
	
	public List<Tag> getCategories() {
		return getTags(productsCache.getProducts());
	}
	
	public List<Tag> getAvailability() {
		return Arrays.asList(getInStock(StockStatus.IN_STOCK), getOutOfStock(StockStatus.OUT_OF_STOCK));
	}
	
	public Tag getInStock(StockStatus stockStatus) {
		List<Product> products = productsCache.getProducts();
		Long count = products.parallelStream().filter(ProductPredicates.stockAvailable()).count();
		return new Tag(stockStatus.getTitle(), count);
	}
	
	public Tag getOutOfStock(StockStatus stockStatus) {
		List<Product> products = productsCache.getProducts();
		Long count = products.parallelStream().filter(ProductPredicates.outOfStock()).count();
		return new Tag(stockStatus.getTitle(), count);
	}
	
	List<Tag> getTags(List<Product> products) {
		return tagFunction.apply(products);
	}
	
	List<String> getDistinctTags(List<Product> products) {
		return products.parallelStream()
				.map(product -> product.getTags())
				.flatMap(tags -> tags.stream())
				.distinct()
				.sorted()
				.collect(Collectors.toList());
	}
	
	private Function<List<Product>, List<Tag>> tagFunction = new Function<List<Product>, List<Tag>>(){

		@Override
		public List<Tag> apply(List<Product> products) {
			List<Tag> tags = new ArrayList<>();
			for(String title : getDistinctTags(products)) {
				Long count = products.parallelStream().filter(ProductPredicates.hasTag(title)).count();
				tags.add(new Tag(title, count));
			} 
			return tags;
		}
	}; 
	
}
