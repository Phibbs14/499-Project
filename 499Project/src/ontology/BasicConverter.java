package ontology;

import s3filecontrol.HeaderValueTuple;

public class BasicConverter implements IStandardEntryConverter {
	private String standardHeader;
	private int index = -1;
	
	public BasicConverter(String header) {
		standardHeader = header;
	}
	
	public BasicConverter(String header, int index) {
		this(header);
		this.index = index;
	}
	
	public HeaderValueTuple convert(HeaderValueTuple original) {
		return new HeaderValueTuple(getHeader(), original.getValue());
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public boolean hasIndexBeenSet() {
		return index != -1;
	}
	
	protected String getHeader() {
		return standardHeader + (hasIndexBeenSet() ? " " + index: "");
	}
}
