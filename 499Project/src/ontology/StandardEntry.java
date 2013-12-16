package ontology;

import s3filecontrol.HeaderValueTuple;

public class StandardEntry implements IStandardEntry, IStandardEntryConverter {
	private String standardHeader;
	
	public StandardEntry(String header) {
		standardHeader = header;
	}
	
	public IStandardEntryConverter getConverter(HeaderValueTuple tuple) {
		// This can return itself, or a metric converter if it needs to convert the value
		// example if header = kV, we might want to convert to V
		// but for Volt, Voltage, or Volts, we can just return this
		
		// TODO Auto-generated method stub
		return this;
	}

	public HeaderValueTuple convert(HeaderValueTuple original) {
		return new HeaderValueTuple(standardHeader, original.getValue());
	}

}
