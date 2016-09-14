package co.za.rightit.catalog.domain;

import java.util.List;
import java.util.Map;

public class ProductSearch {

	private Map<String, List<String>> criteria;
	
	public ProductSearch() {
	}

	public Map<String, List<String>> getCriteria() {
		return criteria;
	}
	
	public boolean isEmpty() {
		return criteria != null && criteria.isEmpty();
	}
	
}
