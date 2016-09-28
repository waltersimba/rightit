package co.za.rightit.catalog.domain;

public class Link {
	
	private String rel;
	
	private String href;

	public String getRel() {
		return rel;
	}

	public String getHref() {
		return href;
	}

	public Link withRel(String rel) {
		this.rel = rel;
		return this;
	}
	
	public Link withHref(String href) {
		this.href = href;
		return this;
	}
	
}
