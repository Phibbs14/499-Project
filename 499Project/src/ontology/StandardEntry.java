package ontology;

import java.util.ArrayList;

import s3filecontrol.HeaderValueTuple;

public class StandardEntry implements IStandardEntry {
	private String standardHeader;
	private ArrayList<String> variationList;
	
	// Remove eventually
	public StandardEntry(String header) {
		standardHeader = header;
		variationList = new ArrayList<String>();
	}
	
	public StandardEntry(String header, ArrayList<String> variations) {
		standardHeader = header;
		variationList = variations;
	}

	// !!! Need to implement the check for conversion, Ex: KV to V
	public IStandardEntryConverter getConverter(HeaderValueTuple tuple) {
		IStandardEntryConverter converter = createConverter(tuple);
		if (NumberCheck.containsNumber(tuple.getHeader()))
			converter.setIndex(NumberCheck.getNumber(tuple.getHeader()));
		return createConverter(tuple);
	}
	
	public IStandardEntryConverter getConverter(HeaderValueTuple tuple, int index) {
		IStandardEntryConverter converter = createConverter(tuple);
		converter.setIndex(index);
		return converter;
	}
	
	protected IStandardEntryConverter createConverter(HeaderValueTuple tuple) {
		return new BasicConverter(standardHeader);
	}

	public String getStandardHeader() {
		return standardHeader;
	}

	public void addVariation(String variation) {
		variationList.add(variation);
	}

	public ArrayList<String> getVariations() {
		return variationList;
	}
}
