package com.rightit.taxibook.service.password;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import com.rightit.taxibook.validation.exception.ApplicationRuntimeException;

public class DefaultPasswordHashService implements PasswordHashService {

	 /**
     * Add additional salt to password hashing
     */
    private static final String HASH_SALT = "d8a8e885-ecce-42bb-8332-894f20f0d8ed";

    private static final int HASH_ITERATIONS = 1000;
    
    /**
     * Hash the password using salt values
     * See https://www.owasp.org/index.php/Hashing_Java
     *
     * @param passwordToHash
     * @return hashed password
     */
	@Override
	public String hashPassword(String passwordToHash) {
		return hashToken(passwordToHash, HASH_SALT );
	}
	
	private String hashToken(String token, String salt) {
        try {
			return byteToBase64(getHash(HASH_ITERATIONS, token, salt.getBytes()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
			throw new ApplicationRuntimeException("Failed to hash password: " + ex.getMessage());
		}
    }

	public static String byteToBase64(byte[] data) {
        return new String(Base64.encodeBase64(data));
    }
	
    public byte[] getHash(int numberOfIterations, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
       MessageDigest digest = MessageDigest.getInstance("SHA-256");
       digest.reset();
       digest.update(salt);
       byte[] input = digest.digest(password.getBytes("UTF-8"));
       for (int i = 0; i < numberOfIterations; i++) {
           digest.reset();
           input = digest.digest(input);
       }
       return input;
   }

}
