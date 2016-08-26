package co.za.rightit.catalog.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
