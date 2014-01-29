package s3filecontrol;

import java.util.ArrayList;

import org.json.simple.JSONArray;

public interface IParsingFile {
	ArrayList<HeaderValueTuple> getFirstSet();
	void setStandardJsonArray(JSONArray jsonArray);
	FileIterator getFileIterator();
}

