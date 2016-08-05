package co.za.rightit.catalog.service;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBuckets;

public class ImageStorageService extends MongoDBFileStorageService {
	
	private Map<String, String> defaultFileExtensionByContentType = new HashMap<String, String>() {
		private static final long serialVersionUID = -4037532372270016038L;

		{
			put("image/jpeg", "jpg");
			put("image/png", "png");
			put("image/gif", "gif");
		}
	};
	
	@Inject
	public ImageStorageService(Provider<MongoDatabase> mongoDatabaseProvider) {
		this.gridFSBucket = GridFSBuckets.create(mongoDatabaseProvider.get());
	}
	
	@Override
	public boolean isContentTypeSupported(String contentType) {
		return defaultFileExtensionByContentType.containsKey(contentType);
	}	
	
}
