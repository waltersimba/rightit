package co.za.rightit.catalog.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.validation.Validator;

import org.joda.money.CurrencyUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import co.za.rightit.catalog.domain.Amount;
import co.za.rightit.catalog.domain.FileInfo;
import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.repository.ProductRepository;
import co.za.rightit.catalog.resources.ProductNotFoundException;
import co.za.rightit.commons.exceptions.ApplicationRuntimeException;
import co.za.rightit.commons.repository.spec.query.FindByIdSpec;
import co.za.rightit.commons.utils.FailedCompletableFutureBuilder;
import co.za.rightit.commons.utils.ValidationUtils;

public class ProductServiceImpl implements ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	private final ProductRepository repository;
	private final Validator validator;
	private final FileStorageService fileStorageService;

	@Inject
	public ProductServiceImpl(ProductRepository repository, FileStorageService fileStorageService, Provider<Validator> validatorProvider) {
		this.repository = repository;
		this.fileStorageService = fileStorageService;
		this.validator = validatorProvider.get();
	}

	@Override
	public void save(ProductRequest request) {
		ValidationUtils.validate(request, validator);
		try {
			repository.save(createProduct(request));
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("Failed to add new product");
			throw new ApplicationRuntimeException("Failed to add new product");
		}
	}

	@Override
	public CompletableFuture<Boolean> update(ProductRequest request) {
		ValidationUtils.validate(request, validator);
		try {
			Product product = toProduct(request);
			return repository.replaceOne(new UpdateProductSpec(product));
		} catch(Exception ex) {
			String errorMessage = String.format("Failed to update product: %s", ex.getMessage());
			LOGGER.error(errorMessage);
			return new FailedCompletableFutureBuilder<Boolean>().build(new ApplicationRuntimeException(errorMessage));
		}
	}
	
	@Override
	public CompletableFuture<Optional<Product>> findProduct(String productId) {
		return repository.findOne(new FindByIdSpec(productId));
	}

	@Override
	public List<Product> findAll() {
		try {
			return repository.findAll();
		} catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("Failed to retrieve products.", ex);
			throw new ApplicationRuntimeException("Failed to retrieve products");
		}
	}

	@Override
	public CompletableFuture<Boolean> updateProductPhoto(String productId, FileInfo fileInfo) {
		CompletableFuture<Product> optionalProduct = getProduct(productId);
		return optionalProduct.thenCompose(product -> {
			if(product.getPhotoId() != null) {
				fileStorageService.deleteFile(product.getPhotoId());
			}
			String photoId = fileStorageService.storeFile(fileInfo);
			LOGGER.info("[{}] Saved image for product {}", photoId, productId);
			product.setPhotoId(photoId);
			return repository.updateOne(new UpdateProductPhotoIdSpec(productId, photoId));
		});		
	}

	private CompletableFuture<Product> getProduct(String productId) {
		CompletableFuture<Optional<Product>> productFuture = repository.findOne(new FindByIdSpec(productId));
		return productFuture.thenCompose(optionalProduct -> {
			CompletableFuture<Product> future = new CompletableFuture<>();
			if(!optionalProduct.isPresent()) {
				String errorMessage = String.format("Could not find product for id: %s.", productId);
				LOGGER.error(errorMessage);
				return new FailedCompletableFutureBuilder<Product>().build(new ProductNotFoundException(errorMessage));
			} else {
				future.complete(optionalProduct.get());
			}
			return future;
		});
	}

	private Product createProduct(ProductRequest request) {
		Amount amount = new Amount(CurrencyUnit.getInstance(request.getCurrency()), request.getPrice());
		Product product = new Product()
				.withAmount(amount)
				.withTitle(request.getTitle())
				.withInventory(request.getInventory())
				.withTags(request.getTags());
		return product;
	}
	
	private Product toProduct(ProductRequest request) {
		return createProduct(request).withId(request.getId());
	}
	
}
