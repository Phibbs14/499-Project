package logging;

public interface ILogger {
	void logError(String errorMessage);
	void logEvent(String eventMessage);
	void logException(Exception exception);
}
