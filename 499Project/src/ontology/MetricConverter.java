package ontology;

import s3filecontrol.HeaderValueTuple;

public class MetricConverter extends BasicConverter {

	public MetricConverter(String header) {
		super(header);
	}
	
	public MetricConverter(String header, int index) {
		super(header, index);
	}

	public HeaderValueTuple convert(HeaderValueTuple original) {
		double value = original.getValueAsDouble();
		
		// convert value
		
		return new HeaderValueTuple(getHeader(), value + "");
	}

	
}
