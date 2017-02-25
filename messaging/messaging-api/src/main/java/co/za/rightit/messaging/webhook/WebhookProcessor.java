package co.za.rightit.messaging.webhook;

/**
 * When an event occurs, an HTTP POST notification message is sent to the defined webhook listener URL.
 * Webhook listener should respond with HTTP 200-level status code to acknowledge receipt.
 * If timeout occurs or webhook listener response with any other status code, we retry 25 times over 3 days.
 * Use HATEOAS self link to get the latest resource from the received payload.
 */
public class WebhookProcessor {

}

