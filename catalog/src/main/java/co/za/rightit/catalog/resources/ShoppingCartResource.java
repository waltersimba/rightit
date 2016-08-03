package co.za.rightit.catalog.resources;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.domain.ShoppingCart;
import co.za.rightit.catalog.repository.ProductRepository;
import co.za.rightit.catalog.service.ShoppingCartService;

@Path("/cart")
public class ShoppingCartResource {

	@Inject
	private ProductRepository productRepository;
	
	@Inject
	private ShoppingCartService shoppingCartService; 
	
	@POST
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response addOrUpdateItem(@PathParam("id")String productId, @QueryParam("quantity") int quantity) {
		Optional<Product> optionalProduct = productRepository.get(productId);
		if(!optionalProduct.isPresent()) {
			throw new ProductNotFoundException(productId);
		} else if(optionalProduct.get().isOutOfStock()) {
			throw new ProductOutOfStockException(optionalProduct.get().getTitle());
		}
		ShoppingCart shoppingCart = shoppingCartService.getShoppingCart();
		shoppingCart.addOrUpdateItem(optionalProduct.get(), quantity);
		return Response.ok(shoppingCartService.getSummary(shoppingCart.getItems())).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getItems() {
		ShoppingCart shoppingCart = shoppingCartService.getShoppingCart();
		return Response.ok(shoppingCart.getItems()).build();
	}
	
	@POST
	@Path("clear")
	@Produces({MediaType.APPLICATION_JSON})
	public Response clearItems() {
		ShoppingCart shoppingCart = shoppingCartService.clearShoppingCart();
		return Response.ok(shoppingCartService.getSummary(shoppingCart.getItems())).build();
	}

	@POST
	@Path("checkout")
	@Produces({MediaType.APPLICATION_JSON})
	public Response checkout() {
		ShoppingCart shoppingCart = shoppingCartService.getShoppingCart();
		//Get customer information
		//Send customer an email with order confirmation
		shoppingCartService.clearShoppingCart();
		return Response.ok().build();
	}
	
}
