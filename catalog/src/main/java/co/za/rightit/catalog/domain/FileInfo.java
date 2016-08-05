package co.za.rightit.catalog.domain;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileInfo {

	private String filename;
	private InputStream inputStream;
	private String contentType;
	private Map<String, Object> metadata;
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	public FileInfo withFilename(String filename) {
		setFilename(filename);
		return this;
	}
	
	public FileInfo withInputStream(InputStream inputStream) {
		setInputStream(inputStream);
		return this;
	}
	
	public FileInfo withContentType(String filename) {
		setContentType(filename);
		return this;
	}
	
	public FileInfo withMetadata(Map<String , Object> metadata) {
		setMetadata(metadata);
		return this;
	}
	
	public FileInfo withMetadata(String key, Object value) {
		@SuppressWarnings("serial")
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put(key, value);
			}
		};
		setMetadata(map);
		return this;
	}
		
}
