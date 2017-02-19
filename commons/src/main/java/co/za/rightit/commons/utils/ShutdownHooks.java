package co.za.rightit.commons.utils;

public class ShutdownHooks {
	
	public static void addShutdownHook(final Service service) {
		Runtime.getRuntime().addShutdownHook(new Thread("ShutdownHook") {
			public void run() {
				service.stopAndWait();
			}
		});
	}
	
}
