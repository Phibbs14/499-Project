package ontology;

import s3filecontrol.HeaderValueTuple;

public class StandardEntry implements IStandardEntry {
	private String standardHeader;
	
	public StandardEntry(String header) {
		standardHeader = header;
	}
	
	// !!! Need to implement the check for conversion, Ex: KV to V
	
	public IStandardEntryConverter getConverter(HeaderValueTuple tuple) {
		if (NumberCheck.containsNumber(tuple.getHeader()))
			return new BasicConverter(
					NumberCheck.getHeaderWithoutNumber(tuple.getHeader()),
					NumberCheck.getNumber(tuple.getHeader()));
		else
			return new BasicConverter(standardHeader);
	}
	
	public IStandardEntryConverter getConverter(HeaderValueTuple tuple, int index) {
		return new BasicConverter(standardHeader, index);
	}

}
