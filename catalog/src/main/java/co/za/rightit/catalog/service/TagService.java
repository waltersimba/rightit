package co.za.rightit.catalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.domain.Tag;

public class TagService {

	@Inject
	private ProductsCache productsCache;
	
	public List<Tag> getTags() {
		return getTags(productsCache.getProducts());
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
				Long count = products.parallelStream().filter(product -> product.getTags().contains(title)).count();
				tags.add(new Tag(title, count));
			} 
			return tags;
		}
	}; 
	
}
