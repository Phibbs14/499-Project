package ontology;

import java.util.ArrayList;

import s3filecontrol.HeaderValueTuple;

public class MetricStandardEntry extends StandardEntry {
	private String baseUnit;
	private String convertToUnit;
	private String convertToPrefix;
	
	
	public MetricStandardEntry(String header, ArrayList<String> variations, String base, String convertTo) throws Exception {
		super(header, variations);
		
		baseUnit = base;
		convertToUnit = convertTo;
		convertToPrefix = MetricConversion.getPrefix(convertToUnit, baseUnit);
		if (convertToPrefix == null)
			throw new Exception("Failed to retrieve the prefix of the convert to unit " + convertToUnit + " of " + header + ".");
		
		addMetricUnitsToVariations();
	}
	
	private void addMetricUnitsToVariations() {
		for (String u : MetricConversion.getMeticAllUnits())
			addVariation(u + baseUnit);
	}
	
	// Override
	protected IStandardEntryConverter createConverter(HeaderValueTuple tuple) {
		String header = NumberCheck.getHeaderWithoutNumber(tuple.getHeader());
		
		if (!header.equals(convertToUnit) && MetricConversion.isValidUnit(header, baseUnit)) {
			String fromPrefix = MetricConversion.getPrefix(header, baseUnit);
			
			if (fromPrefix != null)
				return new MetricConverter(getStandardHeader(), MetricConversion.getConversionFactor(fromPrefix, convertToPrefix));
		}
			
		return super.createConverter(tuple);
	}
}
