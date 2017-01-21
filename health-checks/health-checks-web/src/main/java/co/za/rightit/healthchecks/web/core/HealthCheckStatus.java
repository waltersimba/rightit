package co.za.rightit.healthchecks.web.core;

import java.util.concurrent.atomic.AtomicReference;

import org.joda.time.DateTime;

import com.google.inject.Singleton;

@Singleton
public class HealthCheckStatus {
	private final AtomicReference<DateTime> lastUpdated;
	
	public HealthCheckStatus() {
		lastUpdated = new AtomicReference<DateTime>(DateTime.now());
	}
	
	public void updated() {
		lastUpdated.getAndSet(DateTime.now());
	}
	
	public DateTime getLastUpdated() {
		return lastUpdated.get();
	}
	
}
