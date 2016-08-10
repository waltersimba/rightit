package co.za.rightit.catalog.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import co.za.rightit.catalog.domain.FileInfo;

public abstract class MongoDBFileStorageService implements FileStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBFileStorageService.class);
	public static final long MAX_IMAGE_SIZE = 16 * 1024L * 1024L; //16 MB
	protected GridFSBucket gridFSBucket;

	@Override
	public String storeFile(FileInfo fileInfo) {
		Preconditions.checkArgument(isContentTypeSupported(fileInfo.getContentType()), "Content type not supported: " + fileInfo.getContentType());
		GridFSUploadOptions options = new GridFSUploadOptions().chunkSizeBytes(1024).metadata(new Document(fileInfo.getMetadata()));
		ObjectId fileId = gridFSBucket.uploadFromStream(fileInfo.getFilename(), fileInfo.getInputStream(), options);
		return fileId.toHexString();
	}

	@Override
	public CompletableFuture<Void> serveFile(String fileId, OutputStream outputStream) throws IOException {
		Preconditions.checkNotNull(fileId, "File id cannot be null");
		CompletableFuture<Void> future = new CompletableFuture<Void>();
		gridFSBucket.downloadToStream(new ObjectId(fileId), outputStream);
		return future;
	}

	@Override
	public CompletableFuture<Void> deleteFile(String fileId) {
		Preconditions.checkNotNull(fileId, "File id cannot be null");
		CompletableFuture<Void> future = new CompletableFuture<Void>();
		CompletableFuture.runAsync(() -> {
			try {
				gridFSBucket.delete(new ObjectId(fileId));
			} catch(Exception ex) {
				LOGGER.error("Failed to delete file: " + fileId, ex);
				future.completeExceptionally(ex);
			}
		});		
		return future;
	}

	@Override
	public long getFileSizeMax() {
		return MAX_IMAGE_SIZE;
	}
}
