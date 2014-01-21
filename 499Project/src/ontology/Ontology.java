package ontology;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			// Couldn't find header in the ontology file
			//Case 1: Check if there is a number at the end of the value
			if (tuple.getHeader().matches("[a-z, A-Z]+[0-9]+")) {
				logger.logEvent("Value has a number in it! :" +tuple.getHeader());
				Pattern p1 = Pattern.compile("[a-z, A-Z]+");
				Matcher m = p1.matcher(tuple.getHeader());
				if (m.find()) {
					//Get the base header and look in the file once again for a match
					standardHeader = (String) hashmapForStandardHeader.get(m.group(0).toLowerCase());			//g.group(0) will get the the header value without the number at the end
					int endOfHeaderNameIndex = m.end();		//Char position of the end of the header name before the number
					
					if (standardHeader != null) {
						String headerNameNumberValue = tuple.getHeader().substring(endOfHeaderNameIndex);			//get the integer value at the end of the incoming header value
						standardHeader = standardHeader.concat(headerNameNumberValue);
						//Create the new std entry with the number value appended to the standized header value
						IStandardEntry stdEntry = new StandardEntry(standardHeader);
						logger.logEvent("Creating new standardEntry for : " + standardHeader);
						return stdEntry;
					}
					
				}
			}
		}
		logger.logEvent("Couldn't find standardized value for: " + tuple.getHeader() + " in hashmap");
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
