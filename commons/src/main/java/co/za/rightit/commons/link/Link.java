package co.za.rightit.commons.link;

import com.google.common.base.Preconditions;

public class Link {

	private String rel;
	private String href;
	private String method;

	public Link(String rel, String href, String method) {
		this.rel = Preconditions.checkNotNull(rel, "rel");
		this.href = Preconditions.checkNotNull(href, "href");
		this.method = Preconditions.checkNotNull(method, "method");
	}

	public String getRel() {
		return rel;
	}

	public String getHref() {
		return href;
	}

	public String getMethod() {
		return method;
	}

}
