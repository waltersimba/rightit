package co.za.rightit.catalog.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.repository.ProductRepository;

public class ProductsCache {

	private static Logger LOGGER = LoggerFactory.getLogger(ProductsCache.class);
	private LoadingCache<Long, List<Product>> cache;
	@Inject
	private ProductRepository repository;

	@Inject
	public ProductsCache(@Named("products-cache-expiration")long expirationTimeMinutes) {
		buildCache(expirationTimeMinutes);
	}

	private void buildCache(long expirationTimeMinutes) {
		cache = CacheBuilder.newBuilder()
				.refreshAfterWrite(expirationTimeMinutes, TimeUnit.MINUTES)
				.removalListener(productRemovalListener )
				.build(new ProductLoader());
	}

	public List<Product> getProducts() {
		return cache.getUnchecked(1L);
	}

	private final class ProductLoader extends CacheLoader<Long, List<Product>> {
		@Override
		public List<Product> load(Long key) throws Exception {
			return repository.findAll();
		}

		public ListenableFuture<List<Product>> reload(final Long key, List<Product> value) {
			ExecutorService executor = Executors.newSingleThreadExecutor();
			try {

				ListenableFutureTask<List<Product>> task = ListenableFutureTask.create(new Callable<List<Product>>(){

					@Override
					public List<Product> call() throws Exception {
						return repository.findAll();
					}
				});
				executor.execute(task);
				return task;
			} finally {
				executor.shutdown();
			}
		}
	}

	private RemovalListener<Long, List<Product>> productRemovalListener = new RemovalListener<Long, List<Product>>() {

		@Override
		public void onRemoval(RemovalNotification<Long, List<Product>> notification) {
			LOGGER.info("Refreshing product, productId = {}, was evicted? {}", notification.getKey(), notification.wasEvicted());
		}
	};

}
