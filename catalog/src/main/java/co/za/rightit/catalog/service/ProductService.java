package co.za.rightit.catalog.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import co.za.rightit.catalog.domain.FileInfo;
import co.za.rightit.catalog.domain.Product;

public interface ProductService {

	CompletableFuture<Product> save(ProductRequest request);
	
	CompletableFuture<Boolean> update(ProductRequest request);
	
	CompletableFuture<Optional<Product>> findProduct(String productId);
	
	CompletableFuture<List<Product>> findAll();

	CompletableFuture<Boolean> updateProductPhoto(String productId, FileInfo fileInfo);
}
