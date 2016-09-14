package co.za.rightit.catalog.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import co.za.rightit.catalog.service.TagService;

@Path("tags")
public class TagResource {

	@Inject
	private TagService tagService;
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("categories")
	public Response categories() {
		return Response.ok(tagService.getCategories()).build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("availability")
	public Response availability() {
		return Response.ok(tagService.getAvailability()).build();
	}
	
}
