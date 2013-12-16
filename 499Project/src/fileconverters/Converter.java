package fileconverters;
import logging.ILogger;

import org.json.simple.JSONArray;


public abstract class Converter {
	protected abstract boolean canConvertExtension(String extension);
	protected abstract JSONArray convertToJSON(String fileText);
	
	protected ILogger logger;
	
	public Converter(ILogger logger) {
		this.logger = logger;
	}
}
