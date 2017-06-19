package co.za.rightit.messaging.web.model;

public interface EmailRequestEvent {
	<R extends EmailData> R getData();
	String getDomain();
}
