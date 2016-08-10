package co.za.rightit.catalog.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

import co.za.rightit.catalog.domain.FileInfo;

public interface FileStorageService {

	String storeFile(FileInfo fileInfo);
	
	CompletableFuture<Void> serveFile(String fileId, OutputStream outputStream) throws IOException;
	
	CompletableFuture<Void> deleteFile(String fileId);
	
	boolean isContentTypeSupported(String contentType);
	
	long getFileSizeMax();
}
