package co.za.rightit.catalog.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileStorageService {

	String storeFile(String filename, InputStream in, String contentType);
	
	void serveFile(String fileId, OutputStream outputStream) throws IOException;
	
	void deleteFile(String fileId);
	
	boolean isContentTypeSupported(String contentType);
	
	long getFileSizeMax();
}
