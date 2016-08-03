package co.za.rightit.catalog.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Objects;

public class Product implements Serializable, Comparable<Product> {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String title;
	
	private String description;
	
	private String photoUrl;
	
	private Set<String> tags = new TreeSet<>();
	
	private BigDecimal price;
	
	private int inventory;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	
	public int getInventory() {
		return inventory;
	}

	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	
	public boolean isOutOfStock() {
		return inventory == 0;
	}

	@Override
	public int compareTo(Product o) {
		if(this.title == null) return o.title == null ? 0 : -1;
		return this.title.compareTo(o.title);
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
	
	public Product withId(String id) {
		setId(id);
		return this;
	}
	
	public Product withTitle(String title) {
		setTitle(title);
		return this;
	}
	
	public Product withDescription(String description) {
		setDescription(description);
		return this;
	}
	
	public Product withPhotoUrl(String photoUrl) {
		setPhotoUrl(photoUrl);
		return this;
	}
	
	public Product withTags(String ...tags) {
		for(String tag : tags) {
			this.tags.add(tag);
		}
		return this;
	}
	
	public Product withPrice(BigDecimal price) {
		setPrice(price);
		return this;
	}
	
	public Product withInventory(int inventory) {
		setInventory(inventory);
		return this;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", title=" + title + ", description=" + description + ", photoUrl=" + photoUrl
				+ ", tags=" + tags + ", price=" + price + ", inventory=" + inventory + "]";
	}
	
}
