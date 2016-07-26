package co.za.rightit.taxibook.service.verify;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenVerificationRequest {
	
	@NotNull(message = "Token is required")
	@Pattern(regexp="[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Token is not valid")
	private String token;

	public TokenVerificationRequest(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
