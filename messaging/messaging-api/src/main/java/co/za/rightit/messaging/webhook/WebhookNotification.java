package co.za.rightit.messaging.webhook;

import org.joda.time.DateTime;

public class WebhookNotification<R> {

	private DateTime createdTime;
	/**
	 * Identifies which event triggered the notification message.
	 */
	private String resourceType;
	private String eventType;
	private String summary;
	private R resource;
	private DateTime validUntil;
}
