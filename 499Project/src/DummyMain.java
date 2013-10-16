import java.io.IOException;


public class DummyMain {
	public static void main(String[] args) throws IOException {
		try {
//			fileTransferTest();
			System.out.println("begin!");
			
			FileManager m = new S3FileManager();
			
			m.pullCurrentFileList();
			DataFile file = null;
			while ((file = m.getFileToProcess()) != null)
			{
				processFile(file);
			}
			
			System.out.println("Done");
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private static void fileTransferTest() {
		System.out.println("begin!");
		
		FileManager m = new S3FileManager();
		
		m.pullCurrentFileList();
		DataFile file = null;
		while ((file = m.getFileToProcess()) != null)
		{
			file.setProcessing();
			System.out.println(file.getFileLocation());
			
			file.setProcessed();
			System.out.println(file.getFileLocation());
			
			file.setFailed();
			System.out.println(file.getFileLocation());
			
			file.setUnprocessed();
			System.out.println(file.getFileLocation());
		}
		
		System.out.println("Done");
	}
	
	public static void processFile(DataFile file) {
		// Check file type (extension of key)
		// Setting file to processing may not be necessary
		// convert if necessary
		// run natural language processing
		// send to elastic search
		// move file to processed
		// move to failed if something goes wrong
	}
}
