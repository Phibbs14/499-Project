package s3filecontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import logging.ILogger;

public class S3FileReader {

	private ILogger logger;
	private String foldername;
	private String filename;
	

	public S3FileReader(ILogger logger, String foldername, String filename) {
		this.logger = logger;
		this.foldername = foldername;
		this.filename = filename;
	}

	//Read a file and return the output as a String
	public String getFileAsString() {
		String fileAsString = "";
		
		
		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		Region fileRegion = Region.getRegion(Regions.US_EAST_1);
		s3.setRegion(fileRegion);
		
		try {
    		S3Object object2 = s3.getObject(new GetObjectRequest(this.foldername, this.filename));
    		
    		//save file contents to a String
    		fileAsString = getStringFromInputStream(object2.getObjectContent());
    		
		} catch (Exception e) {
			logger.logException(e);
		}
		
		return fileAsString;
	}

	private static String getStringFromInputStream(InputStream is) throws IOException {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
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
}
