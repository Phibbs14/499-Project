package fileconverters;


import java.util.ArrayList;


import org.json.simple.JSONArray;

import s3filecontrol.DataFile;


public class MainConverter {
	ArrayList<Converter> converters;
	
	public MainConverter() {
		generateConverters();
	}
	
	private void generateConverters() {
		converters = new ArrayList<Converter>();
		converters.add(new JsonConverter());
		converters.add(new CsvConverter());
		converters.add(new XmlConverter());
	}
	
	public JSONArray convert(DataFile file) {
		String extension = file.getExtension();
		
		for (Converter converter : converters) {
			if (converter.canConvertExtension(extension)) {
				JSONArray converted = converter.convertToJSON(file.getFileText());
				file.setConvertedJsonArray(converted);
				return converted;
			}
		}
		
		return null;
	}
}
