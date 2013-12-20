package parser;

import java.util.ArrayList;

import logging.ILogger;
import ontology.IOntology;
import ontology.IStandardEntry;
import ontology.IStandardEntryConverter;
import ontology.Ontology;
import s3filecontrol.HeaderValueTuple;
import s3filecontrol.IParsingFile;

public class ParserFactory implements IParserFactory {
	private ILogger logger;
	private IOntology ontology;

	public ParserFactory(ILogger logger) {
		this.logger = logger;
		ontology = new Ontology(logger);
	}

	public boolean initializeComponents() {
		return ontology.readFromFile("");
	}
	
	public boolean parseFile(IParsingFile dataFile) {
		try {
			ArrayList<HeaderValueTuple> set = dataFile.getFirstSet();
			ArrayList<IStandardEntryConverter> converters = parseSet(set);
			if (converters == null)
				return false;
			
			// conveter.converter(tuple)
						
			
		} catch (Exception ex) {
			logger.logException(ex);
			return false;
		}
		return true;
	}
	
	private ArrayList<IStandardEntryConverter> parseSet(ArrayList<HeaderValueTuple> set) {
		ArrayList<IStandardEntry> standardEntryList = new ArrayList<IStandardEntry>();
		ArrayList<IStandardEntryConverter> converters = new ArrayList<IStandardEntryConverter>();
		
		for (HeaderValueTuple tuple : set) {
			// handle numbers in the header
			
			IStandardEntry ret = ontology.getStandardEntryFor(tuple);
			if (ret == null) {
				logger.logError("Failed to find a match for " + tuple);
				return null;
			}
			
			if (standardEntryList.contains(ret)) { // handle multiple (OK if numbers in header ^^)
				
			} else {
				standardEntryList.add(ret);
				converters.add(ret.getConverter(tuple));
			}
		}
		return converters;
	}
}
