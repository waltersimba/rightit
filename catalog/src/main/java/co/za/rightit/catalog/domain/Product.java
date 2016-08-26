package co.za.rightit.catalog.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.core.Link;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;

import co.za.rightit.commons.domain.DomainObject;

public class Product extends DomainObject implements Comparable<Product> {

	private String title;
		
	private String photoId;
	
	private Set<String> tags = new TreeSet<>();
	
	private Amount amount;
	
	private int inventory;
	
	private List<Link> links = new ArrayList<>();
	
	public Product() {
		super("product");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonIgnore
	public boolean hasPhoto() {
		return getPhotoId() != null;
	}
	
	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public Set<String> getTags() {
		return Collections.unmodifiableSet(tags);
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
	
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	@JsonIgnore
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
		setId(new ObjectId(id));
		return this;
	}
	
	public Product withTitle(String title) {
		setTitle(title);
		return this;
	}
	
	public Product withPhotoId(String photoId) {
		setPhotoId(photoId);
		return this;
	}
	
	public Product withTags(Set<String> tags) {
		this.tags = tags;
		return this;
	}
	
	public Product withTags(String ...tags) {
		for(String tag : tags) {
			this.tags.add(tag);
		}
		return this;
	}
	
	public Product withAmount(Amount price) {
		setAmount(price);
		return this;
	}
	
	public Product withInventory(int inventory) {
		setInventory(inventory);
		return this;
	}
	
}
