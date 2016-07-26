package co.za.rightit.taxibook.service.authentication;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginRequest {
	
	@NotNull(message = "The username is required")
	private String username;
	
	@NotNull(message = "The password is required")
	private String password;
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}	
	
}
