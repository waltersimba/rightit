package co.za.rightit.catalog.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import co.za.rightit.commons.utils.Page;
import co.za.rightit.commons.utils.Pageable;
import co.za.rightit.commons.utils.Pagination;

public class ProductsCache {
	private static Logger LOGGER = LoggerFactory.getLogger(ProductsCache.class);
	private LoadingCache<Key, List<Product>> cache;
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
				.build(new CacheLoader<Key, List<Product>>() {

					@Override
					public List<Product> load(Key key) throws Exception {
						return repository.findAll();
					}

					public ListenableFuture<List<Product>> reload(final Key key, List<Product> value) {
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
				});
	}

	public Page<Product> getProducts(Pageable pageable) {
		List<Product> products = cache.getUnchecked(new Key(1L, pageable)); 
		return paginate(products, pageable);
	}
	
	private Page<Product> paginate(List<Product> items, Pageable pageable) {
		List<Product> paginatedList = items.stream()
				.skip(pageable.getOffset())
				.limit(pageable.getLimit())
				.collect(Collectors.toList());
		Pagination pagination = new Pagination(pageable.getPageNumber(), items.size(), pageable.getLimit());
		return new Page<Product>(paginatedList, pagination);
	}
	
	private class Key {
		private final Long id;
		private final Pageable pageable;
		
		public Key(Long id, Pageable pageable) {
			this.id = id;
			this.pageable = pageable;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		private ProductsCache getOuterType() {
			return ProductsCache.this;
		}

		@Override
		public String toString() {
			return "Key [id=" + id + "]";
		}
		
	}

	private RemovalListener<Key, List<Product>> productRemovalListener = new RemovalListener<Key, List<Product>>() {

		@Override
		public void onRemoval(RemovalNotification<Key, List<Product>> notification) {
			LOGGER.info("Refreshing product, productId = {}, was evicted? {}", notification.getKey(), notification.wasEvicted());
		}
	};

}
