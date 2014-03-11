package logging;

public class StringLogger implements ILogger{
	String result = "";
	
	@Override
	public void logError(String errorMessage) {
		result += errorMessage;
	}

	@Override
	public void logEvent(String eventMessage) {
		logError(eventMessage);
	}

	@Override
	public void logException(Exception exception) {
		logError(exception.getMessage());
		
	}

	@Override
	public String toString() {
		return result;
	}
}
