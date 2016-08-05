package co.za.rightit.catalog.service;

import java.io.IOException;
import java.io.OutputStream;

import co.za.rightit.catalog.domain.FileInfo;

public interface FileStorageService {

	String storeFile(FileInfo fileInfo);
	
	void serveFile(String fileId, OutputStream outputStream) throws IOException;
	
	void deleteFile(String fileId);
	
	boolean isContentTypeSupported(String contentType);
	
	long getFileSizeMax();
}
