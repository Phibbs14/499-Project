package ontology;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonFileParser {
	public static String getStringValue(JSONObject obj, String key) {
		if (obj.containsKey(key)) {
			Object ret = obj.get(key);
			if (ret instanceof String)
				return (String)ret;
		}
		
		return null;
	}
	
	public static JSONArray getJSONArray(JSONObject obj, String key) {
		if (obj.containsKey(key)) {
			Object ret = obj.get(key);
			if (ret instanceof JSONArray)
				return (JSONArray)ret;
		}
		
		return null;
	}

	public static ArrayList<String> getStringArrayList(JSONObject obj, String key) {
		JSONArray jArray = getJSONArray(obj, key);
		if (jArray == null)
			return null;
		ArrayList<String> list = new ArrayList<String>();
		
		for (Object o : jArray) {
			if (o instanceof String)
				list.add((String)o);
			else
				return null;
		}
		
		return list;
	}

	public static Boolean getBooleanValue(JSONObject obj, String key) {
		if (obj.containsKey(key)) {
			Object value = obj.get(key);
			if (value instanceof String) {
				String svalue = (String)value;
				if (svalue.compareToIgnoreCase("true") == 0)
					return true;
				else if (svalue.compareToIgnoreCase("false") == 0)
					return false;
			}
		}
		
		return null;
	}
}
