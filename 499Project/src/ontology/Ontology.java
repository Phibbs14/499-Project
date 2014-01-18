package ontology;

import logging.ILogger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import s3filecontrol.HeaderValueTuple;
import s3filecontrol.S3FileReader;

public class Ontology implements IOntology {

	private ILogger logger;
	
	private JSONObject hashmapForStandardHeader = null;
	
	public Ontology(ILogger logger) {
		this.logger = logger;
	}

	

	public IStandardEntry getStandardEntryFor(HeaderValueTuple tuple) {
		
		logger.logEvent("The header value being searched in hashmap is: "+tuple.getHeader().toLowerCase());
		
		//use the header value as key to find the standardized header value that will be used
		String standardHeader = (String) hashmapForStandardHeader.get(tuple.getHeader().toLowerCase());
		
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

		//Read file contents
		S3FileReader fileReader = new S3FileReader(logger, "OntologyData", filename);
		String result = fileReader.getFileAsString();
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

}
