package co.za.rightit.catalog.module;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.types.ObjectId;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.repository.ProductRepository;
import co.za.rightit.catalog.repository.ShoppingCartRepository;
import co.za.rightit.catalog.repository.ShoppingCartRepositoryImpl;

public class CatalogModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ShoppingCartRepository.class).to(ShoppingCartRepositoryImpl.class).asEagerSingleton();		
	}
	
	@Singleton
	@Provides
	public ProductRepository productRepository() {
		return new ProductRepository() {
			final Map<String, Product> products = new ConcurrentHashMap<>();
			
			@Override
			public boolean update(Product product) {
				boolean updated = false;
				if(products.containsKey(product.getId())) {
					products.put(product.getId(),product);
					updated = true;
				}
				return updated;
			}
			
			@Override
			public boolean insert(Product product) {
				boolean inserted = false;
				if(products.containsKey(product.getId())) {
					products.put(product.getId(), product);
				}
				return inserted;
			}
			
			@Override
			public List<Product> get() {
				if(products.isEmpty()) {
					Product product = new Product()
							.withId(UUID.randomUUID().toString())
							.withTitle("Gold & Green Aviator Sunglasses")
							.withPrice(new BigDecimal(1119.00))
							.withTags("Men")
							.withInventory(3);
					products.put(product.getId(), product);
				}
				return new ArrayList<>(products.values());
			}
			
			@Override
			public Optional<Product> get(String uid) {
				return Optional.ofNullable(products.get(uid));
			}
		};
	}

}
