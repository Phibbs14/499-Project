import com.amazonaws.services.s3.model.S3Object;


public interface FileManager {
	void pullCurrentFileList();
	DataFile getFileToProcess();
	void moveToFailed(DataFile file);
	void moveToProcessed(DataFile file);
	void moveToProcessing(DataFile file);
}
