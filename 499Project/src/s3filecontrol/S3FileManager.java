package s3filecontrol;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;


public class S3FileManager implements FileManager {
	private S3FileControl s3controller;
	private List<S3ObjectSummary> listOfFiles = null;
	private Iterator<S3ObjectSummary> currentFile;
	private String bucketName = "InputData";

	public S3FileManager() {
		s3controller = new S3FileControl();
	}

	public void pullCurrentFileList() {
		listOfFiles = s3controller.getAllObjectSummaries("InputData");
		currentFile = listOfFiles.iterator();
	}

	// Retrieves a file and moves it to processing
	public DataFile getFileToProcess() {
		if (listOfFiles == null || currentFile == null
				|| !currentFile.hasNext())
			return null;

		return new DataFile(currentFile.next(), this);
	}

	public void moveToUnprocessed(DataFile file) {
		moveFileToState(file, DataFile.ProcessingState.Unprocessed);
	}

	public void moveToProcessing(DataFile file) {
		moveFileToState(file, DataFile.ProcessingState.Processing);
	}

	public void moveToFailed(DataFile file) {
		moveFileToState(file, DataFile.ProcessingState.Failed);
	}

	public void moveToProcessed(DataFile file) {
		moveFileToState(file, DataFile.ProcessingState.Processed);
	}

	public S3Object getObjectStreamFromDataFile(DataFile file) {
		return s3controller.getObject(file.getBucket(), file.getFileKey());
	}

	private void moveFileToState(DataFile file, DataFile.ProcessingState state) {
		s3controller.moveObject(bucketName, file.getFileLocation(), bucketName,
				getFolderForState(state) + file.getFileKey());
		file.setState(state);
	}

	public String getFolderForState(DataFile.ProcessingState state) {
		switch (state) {
		case Processing:
			return "Processing/";
		case Processed:
			return "Processed/";
		case Failed:
			return "Failed/";
		default:
			return "";
		}
	}
}
