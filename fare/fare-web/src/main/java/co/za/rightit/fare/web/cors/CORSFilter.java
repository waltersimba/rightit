package co.za.rightit.fare.web.cors;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CORSFilter implements ContainerResponseFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CORSFilter.class);
    /**
     * Add the cross domain data to the output for OPTIONS request (preflight) only
     *
     * @param containerRequest The container request (input)
     * @param containerResponse The container response (output)
     */
    public ContainerResponse filter(ContainerRequest containerRequest, ContainerResponse containerResponse) {
        if(containerRequest.getMethod().equals("OPTIONS")) {
        	LOGGER.debug("Adding CORS for OPTIONS request (preflight) only.");
            Response response = Response
                    .ok()
                    .header("Access-Control-Allow-Headers", containerRequest.getHeaderValue("Access-Control-Request-Headers"))
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .build();
            response = CORSInterceptor.corsResponse(response, containerRequest.getHeaderValue("Origin"));
            containerResponse.setResponse(response);
        }
        return containerResponse;
    }
}
