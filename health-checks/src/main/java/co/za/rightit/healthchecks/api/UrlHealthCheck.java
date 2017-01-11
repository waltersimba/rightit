package co.za.rightit.healthchecks.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;

public class UrlHealthCheck extends HealthCheck {

	protected static final int HTTP_REQUEST_TIMEOUT = 10000; // 10 seconds
	protected static final String TIMEOUT = "%s has timed out.";
	protected static final String RUNNING = "%s is running.";
	protected static final String NOT_REACHABLE = "%s is not reachable.";
	private Logger LOGGER = LoggerFactory.getLogger(UrlHealthCheck.class);
	private String url;
	private boolean secureConnection;
	private int timeout;

	public UrlHealthCheck(String url) {
		this(url, false);
	}

	public UrlHealthCheck(String url, boolean secureConnection) {
		this(url, secureConnection, HTTP_REQUEST_TIMEOUT);
	}

	public UrlHealthCheck(String url, int timeout) {
		this(url, false, HTTP_REQUEST_TIMEOUT);
	}

	public UrlHealthCheck(String url, boolean secureConnection, int timeout) {
		this.url = url;
		this.secureConnection = secureConnection;
		this.timeout = timeout;
	}

	public UrlHealthCheck withUrl(String url) {
		this.url = url;
		return this;
	}

	public UrlHealthCheck withSecureConnection(boolean secureConnection) {
		this.secureConnection = secureConnection;
		return this;
	}

	public UrlHealthCheck withTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	@Override
	protected Result check() throws Exception {
		LOGGER.debug("Checking {} ...", url);
		Result result = null;
		HttpURLConnection connection = null;
		try {
			connection = getConnection(url);
			connection.setConnectTimeout(timeout);
			connection.setRequestMethod("HEAD");
			connection.connect();
			int responseCode = connection.getResponseCode();
			if (responseCode >= 200 && responseCode <= 399) {
				result = HealthCheck.Result.healthy(RUNNING, url);
			} else {
				result = HealthCheck.Result.unhealthy(NOT_REACHABLE, url);
			}
		} catch (SocketTimeoutException ex) {
			LOGGER.error(ex.getMessage());
			result = HealthCheck.Result.unhealthy(TIMEOUT, url);
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
			result = HealthCheck.Result.unhealthy(NOT_REACHABLE, url);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return result;
	}

	protected HttpURLConnection getConnection(String targetUrl) throws IOException {
		return (HttpURLConnection) new URL(getUrlWithoutHttpsIfNecessary(targetUrl)).openConnection();
	}

	protected String getUrlWithoutHttpsIfNecessary(String targetUrl) {
		// Otherwise an exception may be thrown on invalid SSL certificates.
		return !secureConnection ? targetUrl.replaceFirst("^https", "http") : targetUrl;
	}

}
