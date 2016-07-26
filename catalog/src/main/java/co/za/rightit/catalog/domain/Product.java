package co.za.rightit.catalog.domain;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Objects;

import co.za.rightit.commons.domain.DomainObject;

public class Product extends DomainObject implements Comparable<Product> {

	private String name;
	
	private String description;
	
	private String photoUrl;
	
	private Set<String> tags = new TreeSet<>();
	
	private BigDecimal price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	@Override
	public int compareTo(Product o) {
		if(this.name == null) return o.name == null ? 0 : -1;
		return this.name.compareTo(o.name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if(!(obj instanceof Product)) return false;
		Product that = (Product)obj;
		return Objects.equal(this.id, that.id);
	}	
	
}
