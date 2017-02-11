package co.za.rightit.fare.web.cors;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.google.inject.servlet.RequestScoped;

@CORS
@Interceptor
@RequestScoped
public class CORSInterceptor {

    @Inject
    HttpServletRequest request;

    @AroundInvoke
    protected Object invoke(InvocationContext ctx) throws Exception {
        ctx.getParameters();
        if(request.getHeader("Origin") != null) {
            return corsResponse((Response) ctx.proceed(), request.getHeader("Origin"));
        } else {
            return ctx.proceed();
        }
    }

    public static Response corsResponse(Response response, String origin) {
        if(origin != null) {
            return Response
                    .fromResponse(response)
                    .header("Access-Control-Allow-Origin", origin)
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        }
        return response;
    }
}