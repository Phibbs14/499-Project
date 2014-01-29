package ontology;

import java.util.ArrayList;
import java.util.HashMap;

import logging.ConsoleLogger;
import logging.ILogger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import s3filecontrol.HeaderValueTuple;
import s3filecontrol.S3FileReader;

public class Ontology implements IOntology {

	private ILogger logger;
	
	private JSONObject hashmapForStandardHeader = null; // remove later
	private HashMap<String, IStandardEntry> standardEntryHashMap = null;
	
	public Ontology(ILogger logger) {
		this.logger = logger;
	}
	
	private void resetHashMap() {
		if (standardEntryHashMap == null)
			standardEntryHashMap = new HashMap<String, IStandardEntry>();
		else
			standardEntryHashMap.clear();
	}
	
	public IStandardEntry getStandardEntryFor(HeaderValueTuple tuple) {
		String header = tuple.getHeader().toLowerCase();
		
		logger.logEvent("The header value being searched in hashmap is: "+ header);
		
		if (NumberCheck.containsNumber(header))
			header = NumberCheck.getHeaderWithoutNumber(header);
		
		IStandardEntry entry = standardEntryHashMap.get(header);
		
		if (entry != null)
			return entry;
		
		logger.logEvent("Couldn't find standardized value for: " + tuple.getHeader() + " in hashmap");
		return null;
	}
	
	public IStandardEntry getStandardEntryForOld(HeaderValueTuple tuple) {
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
			if (NumberCheck.containsNumber(tuple.getHeader())) {
				logger.logEvent("Value has a number in it! :" +tuple.getHeader());
				
				standardHeader = (String) hashmapForStandardHeader.get(NumberCheck.getHeaderWithoutNumber(tuple.getHeader()));
				
				if (standardHeader != null) {
					//String headerNameNumberValue = tuple.getHeader().substring(endOfHeaderNameIndex);			//get the integer value at the end of the incoming header value
					//standardHeader = standardHeader.concat(headerNameNumberValue);
					//Create the new std entry with the number value appended to the standized header value
					IStandardEntry stdEntry = new StandardEntry(standardHeader);
					logger.logEvent("Creating new standardEntry for : " + standardHeader);
					return stdEntry;
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
			
			JSONObject jsonSet = (JSONObject)parser.parse(result);
			if (!jsonSet.containsKey("ValueList"))
				throw new Exception("Ontology File did not contain the ValueList header.");
			JSONArray jsonArray = (JSONArray)jsonSet.get("ValueList");
			
			resetHashMap();
			
			for (Object obj : jsonArray) {
				try {
					if (!(obj instanceof JSONObject))
						throw new Exception("Error parsing ontology file: could not cast entry in ValueList as JSONObject.");
					IStandardEntry newEntry = createStandardEntryFromJSON((JSONObject)obj);
					if (newEntry == null)
						throw new Exception("Error parsing ontology file: could not create an object from an entry in ValueList.");
					
					for (String variation : newEntry.getVariations())
						standardEntryHashMap.put(variation.toLowerCase(), newEntry);
				} catch (Exception ex) {
					logger.logException(ex);
				}
			}
			System.out.println("Done");
			
		} catch (ParseException ex) {
			logger.logException(ex);
			return false;
		}
		catch (Exception ex) {
			logger.logException(ex);
			return false;
		}
		
		return true;	
	}
	
	public IStandardEntry createStandardEntryFromJSON(JSONObject obj) {
		try {
			String baseUnit = "";
			String convertToUnit = "";
			
			String header = JsonFileParser.getStringValue(obj, "Header");
			
			if (header == null)
				throw new Exception("Error parsing ontology file: Could not read a value's header.");
			ArrayList<String> variations = JsonFileParser.getStringArrayList(obj, "Variations");
			if (variations == null)
				throw new Exception("Error parsing ontology file: Could not read all variations for: " + header + ".");
			Boolean isMetric = JsonFileParser.getBooleanValue(obj, "IsMetric");
			if (isMetric == null)
				throw new Exception("Error parsing ontology file: Could not determine if metric for: " + header + ".");
			
			if (isMetric) {
				baseUnit = JsonFileParser.getStringValue(obj, "BaseUnit");
				convertToUnit = JsonFileParser.getStringValue(obj, "ConvertTo");
				if (!MetricConversion.isValidUnit(convertToUnit, baseUnit))
					throw new Exception("Error parsing ontology file: Invalid metric unit specifications. " + convertToUnit + ", " + baseUnit);
			}
			
			/*
			System.out.println("Header : " + header
					+ "\nvariations: " + t(variations)
					+ "\nIsMetric" + isMetric
					+ (isMetric? 
					"\nBaseUnit : " + baseUnit
					+ "\nConvert To : " + convertToUnit + "\n": ""));
		*/
			if (!isMetric)
				return new StandardEntry(header, variations);
			else 
				return new MetricStandardEntry(header, variations, baseUnit, convertToUnit);
		} catch(Exception ex) {
			logger.logException(ex);
		}
		return null;
	}
	
	public static String t(ArrayList<String> a) {
		String ret ="";
		for (String b : a)
			ret += b + ", ";
		return ret;
	}
	
	public static void main(String a[]) {
		(new Ontology(new ConsoleLogger())).readFromFile("/Users/Shane_Phibbs/Downloads/standardizedValueHashMap2.json");
	}
	
	public boolean readFromFileOld(String filename) {

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
