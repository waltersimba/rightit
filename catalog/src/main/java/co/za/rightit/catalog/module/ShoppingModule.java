package co.za.rightit.catalog.module;

import java.util.Currency;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.provider.CurrencyProvider;
import co.za.rightit.catalog.provider.LocaleProvider;
import co.za.rightit.catalog.repository.ProductRepository;
import co.za.rightit.catalog.repository.ShoppingCartRepository;
import co.za.rightit.catalog.repository.ShoppingCartRepositoryImpl;
import co.za.rightit.catalog.service.FileStorageService;
import co.za.rightit.catalog.service.ImageStorageService;
import co.za.rightit.catalog.service.ProductByIdCache;
import co.za.rightit.catalog.service.ProductService;
import co.za.rightit.catalog.service.ProductServiceImpl;
import co.za.rightit.catalog.service.ShoppingCartService;
import co.za.rightit.catalog.service.ShoppingCartServiceImpl;
import co.za.rightit.commons.provider.ObjectMapperProvider;
import co.za.rightit.commons.provider.ValidatorProvider;
import co.za.rightit.commons.repository.Repository;

public class ShoppingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Long.class).annotatedWith(Names.named("product-cache-expiration")).toInstance(TimeUnit.HOURS.toMinutes(4));
		bind(Long.class).annotatedWith(Names.named("products-cache-expiration")).toInstance(TimeUnit.HOURS.toMinutes(1));
		bind(new TypeLiteral<Repository<Product>>() {}).to(ProductRepository.class);
		bind(ProductService.class).to(ProductServiceImpl.class).asEagerSingleton();
		bind(ProductByIdCache.class);
		bind(Validator.class).toProvider(ValidatorProvider.class).asEagerSingleton();
		bind(ShoppingCartRepository.class).to(ShoppingCartRepositoryImpl.class).asEagerSingleton();
		bind(ShoppingCartService.class).to(ShoppingCartServiceImpl.class);
		bind(FileStorageService.class).to(ImageStorageService.class).asEagerSingleton();;
		bind(Locale.class).toProvider(LocaleProvider.class);
		bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
		bind(new TypeLiteral<Optional<Currency>>() {
		}).toProvider(CurrencyProvider.class);
	}
}
