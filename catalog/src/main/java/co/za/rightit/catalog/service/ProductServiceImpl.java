package co.za.rightit.catalog.service;

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
import co.za.rightit.commons.exceptions.ApplicationRuntimeException;
import co.za.rightit.commons.utils.Page;
import co.za.rightit.commons.utils.Pageable;
import co.za.rightit.commons.utils.ValidationUtils;

public class ProductServiceImpl implements ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Inject
	private ProductRepository repository;
	@Inject
	private FileStorageService fileStorageService;
	@Inject
	private Provider<Validator> validatorProvider;
	@Inject
	private ProductByIdCache productByIdCache;
	@Inject
	private ProductsCache productsCache;
	
	@Override
	public void save(ProductRequest request) {
		ValidationUtils.validate(request, validatorProvider.get());
		try {
			repository.save(createProduct(request));
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("Failed to add new product", ex);
			throw new ApplicationRuntimeException("Failed to add new product");
		}
	}

	@Override
	public Boolean update(ProductRequest request) {
		ValidationUtils.validate(request, validatorProvider.get());
		try {
			Product product = toProduct(request);
			return repository.replaceOne(new UpdateProductSpec(product));
		} catch(Exception ex) {
			LOGGER.error("Failed to update product", ex);
			throw new ApplicationRuntimeException("Failed to update product");
		} finally {
			productByIdCache.invalidateProductCache(request.getId());
		}
	}
	
	@Override
	public Product findProduct(String productId) {
		try {
			return productByIdCache.getProduct(productId);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ApplicationRuntimeException("Failed to find product by ID");
		}
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		try {
			return productsCache.getProducts(pageable);
		} catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("Failed to retrieve products.", ex);
			throw new ApplicationRuntimeException("Failed to retrieve products");
		}
	}

	@Override
	public Boolean updateProductPhoto(String productId, FileInfo fileInfo) {
		Product productFound = findProduct(productId);
		if(productFound.hasPhoto()) {
			fileStorageService.deleteFile(productFound.getPhotoId());
		}
		String photoId = fileStorageService.storeFile(fileInfo);
		LOGGER.info("[{}] Saved image for product {}", photoId, productId);
		productFound.setPhotoId(photoId);
		return repository.updateOne(new UpdateProductPhotoIdSpec(productFound.getId(), photoId));		
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
