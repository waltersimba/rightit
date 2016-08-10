package co.za.rightit.catalog.repository;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.commons.repository.AbstractMongoRepository;

public class ProductRepository extends AbstractMongoRepository<Product> {
	
	@Override
	public MongoCollection<Document> getCollection() {
		return mongoDatabaseProvider.get().getCollection("products");
	}

	@Override
	public Class<Product> getResultType() {
		return Product.class;
	}

}
