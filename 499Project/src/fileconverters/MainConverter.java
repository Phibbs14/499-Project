package fileconverters;


import java.util.ArrayList;

import logging.ILogger;

import org.json.simple.JSONArray;

import s3filecontrol.DataFile;


public class MainConverter implements IFileFormatConverter {
	private ArrayList<Converter> converters;
	private ILogger logger;
	
	public MainConverter(ILogger logger) {
		this.logger = logger;
		generateConverters();
	}
	
	private void generateConverters() {
		converters = new ArrayList<Converter>();
		converters.add(new JsonConverter(logger));
		converters.add(new CsvConverter(logger));
		converters.add(new XmlConverter(logger));
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
