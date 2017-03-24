package co.za.rightit.messaging.email;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class FileEmailAccountRepository implements EmailAccountRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileEmailAccountRepository.class);
	private final Path jsonFilePath;
	
	public FileEmailAccountRepository(Path jsonFilePath) {
		this.jsonFilePath = Preconditions.checkNotNull(jsonFilePath, "jsonFilePath");
		Preconditions.checkArgument(Files.exists(jsonFilePath), "json file does not exist");
		Preconditions.checkArgument(jsonFilePath.endsWith(".json"), "json extention expected");
	}
		
	@Override
	public Optional<EmailAccount> findEmailAccount(String domain) {
		LOGGER.info("Scanning for email account with domain={} in file={}", domain, jsonFilePath.getFileName());
		try (InputStream inputStream = Files.newInputStream(jsonFilePath);
				JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"))) {
			Gson gson = new GsonBuilder().create();
			EmailAccount emailAccount = null;
			// Read file in stream mode
			reader.beginArray();
			while (reader.hasNext()) {
				// Read data into object model one at a time
				emailAccount = gson.fromJson(reader, EmailAccount.class);
				if (domain != null && emailAccount != null && domain.equals(emailAccount.getDomain())) {
					LOGGER.info("Email account with domain={} found.", domain);
					break;
				}
			}
			if(emailAccount == null) {
				LOGGER.warn("Email account with domain={} NOT found", domain);
			}
			return Optional.ofNullable(emailAccount);
		} catch (UnsupportedEncodingException uee) {
			LOGGER.error(String.format("Failed to read email account, domain=%s", domain), uee);
			throw new RuntimeException(uee);
		} catch (IOException ioe) {
			LOGGER.error(String.format("Failed to read email account, domain=%s", domain), ioe);
			throw new RuntimeException(ioe);
		}
	}

}
