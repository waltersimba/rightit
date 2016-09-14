package co.za.rightit.catalog.domain;

public class Tag {

	private String title;
	
	private Long count;

	public Tag() {}
	
	public Tag(String title, Long count) {
		this.title = title;
		this.count = count;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
