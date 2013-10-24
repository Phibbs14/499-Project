/*
 * Implements the Converter interface
 * Takes a JSON file as a string and returns it as a JSONArray
 */

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JsonConverter implements Converter{

	public boolean canConvertExtension(String extension) {
		return extension.equalsIgnoreCase(".json");
	}

	public JSONArray convertToJSON(String fileText) {
		try
		{
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray)parser.parse(fileText);
			
			return array;
		}
		catch (ParseException ex) {
			return null;
		}
	}
}
