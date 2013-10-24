/*
 * Implements the Converter interface
 * Takes a CSV file as a string and returns the corresponding JSON array 
 */


import org.json.simple.JSONArray;



public class CsvConverter implements Converter{

	public boolean canConvertExtension(String extension) {
		return extension.equalsIgnoreCase(".csv");
	}

	public JSONArray convertToJSON(String fileText) {
		// TODO Auto-generated method stub
		return null;
	}

}
