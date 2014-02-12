package s3filecontrol;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonFileIterator implements FileIterator {
	private Iterator jArrayIterator = null;
	
	public JsonFileIterator(JSONArray jarray) {
		jArrayIterator = jarray.iterator();
	}
	
	public boolean hasNext() {
		return jArrayIterator.hasNext();
	}

	public HeaderValueTuple[] next() {
		JSONObject set = (JSONObject)jArrayIterator.next();
		
		HeaderValueTuple[] rSet = new HeaderValueTuple[set.keySet().size()];
		
		int i = 0;
		
		for (Object key : set.keySet()) 
			rSet[i++] = new HeaderValueTuple(key.toString(), set.get(key).toString());
		
		return rSet;
	}

	public void remove() {
	}

}
