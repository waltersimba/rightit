package co.za.rightit.catalog.service;

import java.io.IOException;
import java.io.OutputStream;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.common.base.Preconditions;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import co.za.rightit.catalog.domain.FileInfo;

public abstract class MongoDBFileStorageService implements FileStorageService {

	protected GridFSBucket gridFSBucket;
	
	@Override
	public String storeFile(FileInfo fileInfo) {
		Preconditions.checkArgument(isContentTypeSupported(fileInfo.getContentType()), "Content type not supported: " + fileInfo.getContentType());
		GridFSUploadOptions options = new GridFSUploadOptions().chunkSizeBytes(1024).metadata(new Document(fileInfo.getMetadata()));
		ObjectId fileId = gridFSBucket.uploadFromStream(fileInfo.getFilename(), fileInfo.getInputStream(), options);
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
