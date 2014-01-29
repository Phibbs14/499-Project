package s3filecontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.amazonaws.services.s3.model.S3ObjectSummary;

public class DataFile implements IParsingFile {
	private S3ObjectSummary summary;
	private S3FileManager fileManager;
	private ProcessingState state = ProcessingState.Unprocessed;
	private JSONArray jsonArray = null;
	private JSONArray standardJsonArray = null;

	public DataFile(S3ObjectSummary fileSummary, S3FileManager fm) {
		summary = fileSummary;
		fileManager = fm;
	}

	public String getExtension() {
		String filename = summary.getKey();
		int i = filename.lastIndexOf('.');

		return i == -1 ? null : filename.substring(i);
	}

	public String getFileText() {
		return getStringFromInputStream(getFileStream());
	}

	private static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public void setConvertedJsonArray(JSONArray array) {
		jsonArray = array;
	}

	public boolean isFileConverted() {
		return jsonArray != null;
	}

	public JSONArray getConvertedJsonArray() {
		return jsonArray;
	}

	// FILE MOVING

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

	// IParsingFile
	public ArrayList<HeaderValueTuple> getFirstSet() {
		try {
			if (jsonArray == null || jsonArray.size() == 0)
				return null;

			JSONObject firstSet = (JSONObject) jsonArray.get(0);
			
			ArrayList<HeaderValueTuple> list = new ArrayList<HeaderValueTuple>();
			
			for (Object key : firstSet.keySet())
				list.add(new HeaderValueTuple(key.toString(), firstSet.get(key).toString()));
			return list;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public FileIterator getFileIterator() {
		if (jsonArray == null)
			return null;
		return new JsonFileIterator(jsonArray);
	}
	
	public void setStandardJsonArray(JSONArray jarray) {
		standardJsonArray = jarray;
	}
	
	public JSONArray getStandardJsonArray() {
		return standardJsonArray;
	}
}
