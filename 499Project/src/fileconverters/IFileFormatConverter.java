package fileconverters;

import org.json.simple.JSONArray;

import s3filecontrol.DataFile;

public interface IFileFormatConverter {
	JSONArray convert(DataFile file);
}
