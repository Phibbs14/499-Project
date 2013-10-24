import java.io.InputStream;

import com.amazonaws.services.s3.model.S3ObjectSummary;

public class DataFile {
	private S3ObjectSummary summary;
	private S3FileManager fileManager;
	private ProcessingState state = ProcessingState.Unprocessed;

	public DataFile(S3ObjectSummary fileSummary, S3FileManager fm) {
		summary = fileSummary;
		fileManager = fm;
	}

	public void setProcessing() {
		fileManager.moveToProcessing(this);
	}

	public void setFailed() {
		fileManager.moveToFailed(this);
	}

	public void setProcessed() {
		fileManager.moveToProcessed(this);
	}
	
	public void setUnprocessed() {
		fileManager.moveToUnprocessed(this);
	}

	// Alternatively it's possible to save the it to a File
	// Need to figure out how we can convert file to JSON
	public InputStream getFileStream() {
		return fileManager.getObjectStreamFromDataFile(this).getObjectContent();
	}

	public String getFileKey() {
		return summary.getKey();
	}

	public String getBucket() {
		return summary.getBucketName();
	}

	public String getFileLocation() {
		return fileManager.getFolderForState(state) + getFileKey();
	}

	public void setState(ProcessingState state) {
		this.state = state;
	}

	public enum ProcessingState {
		Unprocessed, Processing, Processed, Failed
	}
}
