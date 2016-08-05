package co.za.rightit.catalog.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.common.base.Preconditions;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

public abstract class MongoDBFileStorageService implements FileStorageService {

	protected GridFSBucket gridFSBucket;
	
	@Override
	public String storeFile(String filename, InputStream inputStream, String contentType) {
		Preconditions.checkArgument(isContentTypeSupported(contentType), "Content type not supported: " + contentType);
		GridFSUploadOptions options = new GridFSUploadOptions().chunkSizeBytes(1024).metadata(new Document("type", "media"));
		ObjectId fileId = gridFSBucket.uploadFromStream(filename, inputStream, options);
		return fileId.toHexString();
	}

	@Override
	public void serveFile(String fileId, OutputStream outputStream) throws IOException {
		Preconditions.checkNotNull(fileId, "File id cannot be null");
		gridFSBucket.downloadToStream(new ObjectId(fileId), outputStream);
	}
	
	@Override
	public void deleteFile(String fileId) {
		Preconditions.checkNotNull(fileId, "File id cannot be null");
		gridFSBucket.delete(new ObjectId(fileId));		
	}

	@Override
	public long getFileSizeMax() {
		return 30000000; //50 MB
	}
}
