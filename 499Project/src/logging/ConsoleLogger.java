package logging;

public class ConsoleLogger implements ILogger {

	public void logError(String errorMessage) {
		System.out.println("Error has occured: " + errorMessage);
	}

	public void logEvent(String eventMessage) {
		System.out.println(eventMessage);
	}

	public void logException(Exception exception) {
		System.out.println("Exception has occured: " + exception.getMessage());
	}

}
