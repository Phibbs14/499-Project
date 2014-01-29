package ontology;

import s3filecontrol.HeaderValueTuple;

public class MetricConverter extends BasicConverter {
	private double multiplierFactor;
	
	public MetricConverter(String header, double factor) {
		super(header);
		multiplierFactor = factor;
	}

	public HeaderValueTuple convert(HeaderValueTuple original) {
		double value = original.getValueAsDouble();
		return new HeaderValueTuple(getHeader(), value * multiplierFactor + "");
	}

	
}
