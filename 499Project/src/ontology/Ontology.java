package ontology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import logging.ILogger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import s3filecontrol.HeaderValueTuple;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class Ontology implements IOntology {

	private ILogger logger;
	
	private JSONObject hashmapForStandardHeader = null;
	
	public Ontology(ILogger logger) {
		this.logger = logger;
	}

	

	public IStandardEntry getStandardEntryFor(HeaderValueTuple tuple) {
		
		//readFromFile("standardizedValueHashMap.json");
		
		logger.logEvent("The header value being searched in hashmap is: "+tuple.getHeader());
		//use the header value to as key to find the standardized header value that will be used
		String standardHeader = (String) hashmapForStandardHeader.get(tuple.getHeader());
		
		//Check to see if a standard header value was found in the hash map. If it's found then create a StandardEntry object for it.
		if (standardHeader != null) {
			logger.logEvent("Creating new standardEntry for : " + standardHeader);
			IStandardEntry stdEntry = new StandardEntry(standardHeader);
			return stdEntry;
		} else {
			logger.logEvent("Couldn't find standardized value for: " + tuple.getHeader() + " in hashmap");
		}
		
		return null;
	}

	public boolean outputToFile(String filename) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean readFromFile(String filename) {
		
		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_EAST_1);
		s3.setRegion(usWest2);
		String result = "";
		
		try {
    		S3Object object2 = s3.getObject(new GetObjectRequest("OntologyData", filename));
    		
    		//save file contents in the string called result
    		result = getStringFromInputStream(object2.getObjectContent());
    		
		} catch (Exception e) {
			logger.logException(e);
			return false;
		}
	
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(result);
			
			//This jsonObject will act as the hashmap to find the standardized header value
			this.hashmapForStandardHeader = (JSONObject) obj;
		} catch (ParseException ex) {
			logger.logException(ex);
			return false;
		}
		
		return true;	
	}
	
	//Need this method for reading file from S3
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
