package co.za.rightit.catalog.module;

import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Validator;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.provider.CurrencyProvider;
import co.za.rightit.catalog.provider.LocaleProvider;
import co.za.rightit.catalog.repository.ProductRepository;
import co.za.rightit.catalog.repository.ShoppingCartRepository;
import co.za.rightit.catalog.repository.ShoppingCartRepositoryImpl;
import co.za.rightit.catalog.service.FileStorageService;
import co.za.rightit.catalog.service.ImageStorageService;
import co.za.rightit.catalog.service.ProductService;
import co.za.rightit.catalog.service.ProductServiceImpl;
import co.za.rightit.catalog.service.ShoppingCartService;
import co.za.rightit.catalog.service.ShoppingCartServiceImpl;
import co.za.rightit.commons.provider.ValidatorProvider;
import co.za.rightit.commons.repository.Repository;

public class ShoppingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<Repository<Product>>() {}).to(ProductRepository.class);
		bind(ProductService.class).to(ProductServiceImpl.class).asEagerSingleton();
		bind(Validator.class).toProvider(ValidatorProvider.class).asEagerSingleton();
		bind(ShoppingCartRepository.class).to(ShoppingCartRepositoryImpl.class).asEagerSingleton();
		bind(ShoppingCartService.class).to(ShoppingCartServiceImpl.class);
		bind(FileStorageService.class).to(ImageStorageService.class).asEagerSingleton();;
		bind(Locale.class).toProvider(LocaleProvider.class);
		bind(new TypeLiteral<Optional<Currency>>() {
		}).toProvider(CurrencyProvider.class);
	}
}
