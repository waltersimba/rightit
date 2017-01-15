package co.za.rightit.healthchecks.mongo.util;

import java.util.HashMap;
import java.util.Map;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;

import co.za.rightit.healthchecks.constants.HealthCheckConstants;
import co.za.rightit.healthchecks.model.HealthCheck;

public class HealthChecksDataFactory {
	
	public static MongoCollection getHealthChecksCollection() {
        Fongo fongo = new Fongo("mongo in-memory server");
        DB db = fongo.getDB("config");
        Jongo jongo = new Jongo(db);
        MongoCollection checks = jongo.getCollection("healthchecks");
        checks.ensureIndex("{name:1}", "{unique: true}");
        return checks;
    }
		
	public static HealthCheck createHealthCheck() {
		@SuppressWarnings("serial")
		Map<String, Object> properties = new HashMap<String, Object>() {
			{
				put(HealthCheckConstants.HEALTHY, Boolean.TRUE);
				put(HealthCheckConstants.TIMEOUT, HealthCheckConstants.DEFAULT_TIMEOUT_SECONDS);
				put(HealthCheckConstants.TARGET_URL, "http://wwww.example.co.za");
				put(HealthCheckConstants.INTERVAL, HealthCheckConstants.DEFAULT_INTERVAL_SECONDS);
				put(HealthCheckConstants.GRACE, HealthCheckConstants.DEFAULT_GRACE_SECONDS);
				put(HealthCheckConstants.CHANNELS, new Object[]{});
			}
		};
		return new HealthCheck()
				.withName("example-health-check")
				.withType(HealthCheck.Type.URL.getIdentifier())
				.withProperties(properties);
	}
}
