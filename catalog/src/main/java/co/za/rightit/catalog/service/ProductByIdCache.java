package co.za.rightit.catalog.service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.repository.ProductRepository;
import co.za.rightit.catalog.resources.ProductNotFoundException;
import co.za.rightit.commons.repository.spec.query.FindByIdSpec;

public class ProductByIdCache {
	private static Logger LOGGER = LoggerFactory.getLogger(ProductByIdCache.class);
	private LoadingCache<String, Product> productCache;
	@Inject
	private ProductRepository repository;

	@Inject
	public ProductByIdCache(@Named("product-cache-expiration")int expirationTimeMinutes) {
		buildProductCache(expirationTimeMinutes);
	}

	private void buildProductCache(int expirationTimeMinutes) {
		productCache = CacheBuilder.newBuilder()
				.expireAfterAccess(expirationTimeMinutes, TimeUnit.MINUTES)
				.removalListener(productRemovalListener )
				.build(new CacheLoader<String, Product>() {

					@Override
					public Product load(String key) throws Exception {
						Optional<Product> optionalProduct = repository.findOne(new FindByIdSpec(key));
						if(!optionalProduct.isPresent()) {
							throw new ProductNotFoundException("Failed to find product by ID");
						}
						return optionalProduct.get();
					}
				});
	}
	
	public Product getProduct(String id) throws ExecutionException {
		return productCache.get(id);
	}
	
	public void invalidateProductCache(String id) {
		productCache.invalidate(id);
	}

	private RemovalListener<String, Product> productRemovalListener = new RemovalListener<String, Product>() {

		@Override
		public void onRemoval(RemovalNotification<String, Product> notification) {
			LOGGER.info("Refreshing product, productId = {}, was evicted? {}", notification.getKey(), notification.wasEvicted());
		}
	};

}
