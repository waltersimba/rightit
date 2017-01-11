package co.za.rightit.healthchecks.api;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck.Result;

import co.za.rightit.healthchecks.api.UrlHealthCheck;

public class UrlHealthCheckTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlHealthCheckTest.class);
	private static final String HTTP_URL = "http://www.example.com";
	private static final String HTTPS_URL = "https://www.example.com";
	protected UrlHealthCheck objectUnderTest;
	protected HttpURLConnection mockHttpURlConnection = null;
	
	@Before
	public void executeBeforeEachTest() throws Exception {
		objectUnderTest = Mockito.spy(new UrlHealthCheck(HTTP_URL));
		mockHttpURlConnection = Mockito.mock(HttpURLConnection.class);
		createVoidFunctionWithParametersAnswer().when(mockHttpURlConnection).setConnectTimeout(Mockito.anyInt());
		createVoidFunctionWithParametersAnswer().when(mockHttpURlConnection).setRequestMethod(Mockito.anyString());
			
		Mockito.doReturn(mockHttpURlConnection).when(objectUnderTest).getConnection(Mockito.anyString());
	}
	
	@After
	public void executeAfterEachTest() {
		objectUnderTest = null;
		mockHttpURlConnection = null;
	}
	
	@Test
	public void testRunningHealthCheckWhenHttpResponseCodeIs200() throws Exception {
		LOGGER.debug("testRunningHealthCheckWhenHttpResponseCodeIs200");
		//given
		Mockito.doNothing().when(mockHttpURlConnection).connect();
		Mockito.doReturn(200).when(mockHttpURlConnection).getResponseCode();
		//when
		Result result = objectUnderTest.check();
		//then
		assertTrue(result.isHealthy());
		assertEquals(String.format(UrlHealthCheck.RUNNING, HTTP_URL), result.getMessage());	
	}
	
	@Test
	public void testRunningHealthCheckWhenHttpResponseCodeIs304() throws Exception {
		LOGGER.debug("testRunningHealthCheckWhenHttpResponseCodeIs304");
		//given
		Mockito.doNothing().when(mockHttpURlConnection).connect();
		Mockito.doReturn(304).when(mockHttpURlConnection).getResponseCode();
		//when
		Result result = objectUnderTest.check();
		//then
		assertTrue(result.isHealthy());
		assertEquals(String.format(UrlHealthCheck.RUNNING, HTTP_URL), result.getMessage());	
	}
	
	@Test
	public void testNotReacheableHealthCheckWhenIOExceptionIsThrown() throws Exception {
		LOGGER.debug("testNotReacheableHealthCheckWhenIOExceptionIsThrown");
		//given
		Mockito.doThrow(new IOException("Fake error")).when(mockHttpURlConnection).connect();
		//when
		Result result = objectUnderTest.check();
		//then
		assertFalse(result.isHealthy());
		assertEquals(String.format(UrlHealthCheck.NOT_REACHABLE, HTTP_URL), result.getMessage());
	}
	
	@Test
	public void testNotReacheableHealthCheckWhenHttpResponseCodeIs401() throws Exception {
		LOGGER.debug("testNotReacheableHealthCheckWhenHttpResponseCodeIs401");
		//given
		Mockito.doNothing().when(mockHttpURlConnection).connect();
		Mockito.doReturn(401).when(mockHttpURlConnection).getResponseCode();
		//when
		Result result = objectUnderTest.check();
		//then
		assertFalse(result.isHealthy());
		assertEquals(String.format(UrlHealthCheck.NOT_REACHABLE, HTTP_URL), result.getMessage());
	}
	
	@Test
	public void testTimeoutHealthyCheck() throws Exception {
		LOGGER.debug("testTimeoutHealthyCheck");
		//given
		Mockito.doThrow(new SocketTimeoutException("Fake timeout error")).when(mockHttpURlConnection).connect();
		//when
		Result result = objectUnderTest.check();
		//then
		assertFalse(result.isHealthy());
		assertEquals(String.format(UrlHealthCheck.TIMEOUT, HTTP_URL), result.getMessage());
	}
	
	@Test
	public void testShouldConvertHttpsToHttp() throws Exception {
		LOGGER.debug("testShouldConvertHttpsToHttp");
		//given
		Mockito.doNothing().when(mockHttpURlConnection).connect();
		Mockito.doReturn(200).when(mockHttpURlConnection).getResponseCode();
		//when
		objectUnderTest.withSecureConnection(false);
		//then
		assertEquals(HTTP_URL, objectUnderTest.getUrlWithoutHttpsIfNecessary(HTTPS_URL));		
	}
	
	@Test
	public void testShouldNotConvertHttpsToHttp() throws Exception {
		LOGGER.debug("testShouldNotConvertHttpsToHttp");
		//given
		Mockito.doNothing().when(mockHttpURlConnection).connect();
		Mockito.doReturn(200).when(mockHttpURlConnection).getResponseCode();
		//when
		objectUnderTest.withSecureConnection(true);
		//then
		assertEquals(HTTPS_URL, objectUnderTest.getUrlWithoutHttpsIfNecessary(HTTPS_URL));
	}
	
	@Test
	public void testShouldNotChangeHttpProtocol() throws Exception {
		LOGGER.debug("testShouldNotChangeHttpProtocol");
		//given
		Mockito.doNothing().when(mockHttpURlConnection).connect();
		Mockito.doReturn(200).when(mockHttpURlConnection).getResponseCode();
		//when
		objectUnderTest.withSecureConnection(false);
		//then
		assertEquals(HTTP_URL, objectUnderTest.getUrlWithoutHttpsIfNecessary(HTTP_URL));
	}
	
	
	private Stubber createVoidFunctionWithParametersAnswer() {
		return Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		});	
	}

}
