package co.za.rightit.catalog.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import co.za.rightit.catalog.domain.FileInfo;
import co.za.rightit.catalog.domain.Product;
import co.za.rightit.catalog.repository.ProductRepository;
import co.za.rightit.catalog.service.FileStorageService;

@Path("products")
public class ProductResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);
	@Inject
	private ProductRepository productRepository;

	@Inject
	private FileStorageService fileStorageService;

	@Context
	private UriInfo uriInfo;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response products() {
		try {
			List<Product> products = productRepository.get();
			products.forEach((product) -> product.getLinks().addAll(new LinksFunction().apply(product)));
			return Response.ok(products).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException(ex);
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public Response product(@PathParam("id") String productId) {
		try {
			Optional<Product> optionalProduct = productRepository.get(productId);
			if (!optionalProduct.isPresent()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			Product product = optionalProduct.get();
			product.getLinks().addAll(new LinksFunction().apply(product));
			return Response.ok(product).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	@POST
	@Path("{id}/photo")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response uploadImage(@PathParam("id") String productId, @Context HttpServletRequest request) {
		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new WebApplicationException("Not a valid file upload request.");
		}
		Optional<Product> optionalProduct = productRepository.get(productId);
		if (!optionalProduct.isPresent()) {
			throw new ProductNotFoundException(productId);
		}

		try {
			FileItem item = getFileItem(request);
			if (item != null) {
				Product product = optionalProduct.get();
				try {
					if (product.getPhotoId() != null) {
						fileStorageService.deleteFile(product.getPhotoId());
					}
				} finally {
					String photoId = fileStorageService.storeFile(new FileInfoFunction(product).apply(item));
					LOGGER.info("[{}] Saved image for product {}", photoId, productId);
					product.setPhotoId(photoId);
					productRepository.update(product);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Response.ok().build();
	}

	@GET
	@Path("photo/{id}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response downloadImage(@PathParam("id") String photoId) {

		StreamingOutput imageStream = new StreamingOutput() {

			@Override
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				fileStorageService.serveFile(photoId, outputStream);
				outputStream.flush();
			}
		};
		return Response.ok(imageStream)
				.header("cache-control", "public, max-age=" + TimeUnit.SECONDS.convert(30, TimeUnit.DAYS)).build();
	}

	private FileItem getFileItem(HttpServletRequest request) throws FileUploadException, IOException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		upload.setFileSizeMax(fileStorageService.getFileSizeMax());
		Iterator<FileItem> iterator = items.iterator();
		while (iterator.hasNext()) {
			FileItem item = iterator.next();
			if (!item.isFormField() && fileStorageService.isContentTypeSupported(item.getContentType())) {
				return item;
			}
		}
		return null;
	}

	private class FileInfoFunction implements Function<FileItem, FileInfo> {

		private final Product product;

		public FileInfoFunction(Product product) {
			this.product = product;
		}

		@Override
		public FileInfo apply(FileItem item) {
			try {
				return new FileInfo().withFilename(item.getName()).withContentType(item.getContentType())
						.withInputStream(item.getInputStream()).withMetadata("product", product.getId());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

	}

	private class LinksFunction implements Function<Product, List<Link>> {

		private List<Link> links = new ArrayList<>();

		@Override
		public List<Link> apply(Product product) {
			UriBuilder builder = uriInfo.getRequestUriBuilder();
			links.add(Link.fromUri(URI.create(builder.path("/{id}").build(product.getId()).toString())).rel("self")
					.build());
			links.add(Link.fromUri(URI.create(builder.path("/photo/{id}").build(product.getPhotoId()).toString()))
					.rel("photo").build());
			return links;
		}
	};
}
