package co.za.rightit.healthchecks.constants;

public interface HealthCheckConstants {
	
	final static String TIMEOUT = "timeout";
	final static String HEALTHY = "healthy";
	final static String CHANNELS = "channels";
	final static String TARGET_URL = "target_url";
	final static String LAST_PING = "last_ping";
	final static String INTERVAL = "interval";
	final static String GRACE = "grace";
	
	final static int DEFAULT_TIMEOUT_SECONDS = 10; //10 seconds
	final static int DEFAULT_INTERVAL_SECONDS = 5 * 60; //5 minutes
	final static int DEFAULT_GRACE_SECONDS = 0;
	
}
