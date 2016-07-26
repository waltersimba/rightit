package co.za.rightit.taxibook.service.verify;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;

@XmlRootElement
public class PasswordRequest {
	@Length(min=8, max=30, message = "The password must be between {min} and {max} characters")
    @NotNull(message = "The password is required")
	private String password;
	@NotNull(message = "Token is required")
	@Pattern(regexp="[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Token is not valid")
	private String token;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
