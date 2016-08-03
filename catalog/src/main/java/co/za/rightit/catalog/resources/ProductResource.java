package co.za.rightit.catalog.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import co.za.rightit.catalog.repository.ProductRepository;

@Path("products")
public class ProductResource {

	@Inject
	private ProductRepository productRepository;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response products() {
		try {
			return Response.ok(productRepository.get()).build();	
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException(ex);
		}	
	}
}
