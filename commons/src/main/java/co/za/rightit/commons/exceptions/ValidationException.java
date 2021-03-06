package co.za.rightit.commons.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ValidationException extends WebApplicationException {

	private static final long serialVersionUID = 1L;
    
	private String errorMessage;
    
	private String developerMessage;
    
	private List<ValidationError> errors = null;

    public ValidationException() {
        errorMessage = "Validation Error";
        developerMessage = "The data passed in the request was invalid. Please check and resubmit";
        errors = new ArrayList<ValidationError>();
    }

    public ValidationException(String message) {
        super();
        errorMessage = message;
    }

    public ValidationException(Set<? extends ConstraintViolation<?>> violations) {
        this();
        for(ConstraintViolation<?> constraintViolation : violations) {
            ValidationError error = new ValidationError();
            error.setMessage(constraintViolation.getMessage());
            error.setPropertyName(constraintViolation.getPropertyPath().toString());
            errors.add(error);
        }
    }

    @Override
    public Response getResponse() {
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(getErrorResponse()).build();
    }

    public ErrorResponse getErrorResponse() {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(Integer.toString(Response.Status.BAD_REQUEST.getStatusCode()));
        response.setApplicationMessage(developerMessage);
        response.setConsumerMessage(errorMessage);
        response.setValidationErrors(errors);
        return response;
    }

}
