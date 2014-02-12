package ontology;

public class MetricConversion {
	private static final String[] metricUnits = {"p", "n", "u", "m", "c", "d", "da", "h", "k", "M", "G", "T"};
	
	public static String[] getMeticAllUnits() {
		return metricUnits;
	}
	
	public static boolean isValidUnit(String unitWithPrefix, String baseUnit) {
		String prefix = getPrefix(unitWithPrefix, baseUnit);
		
		if (prefix == null)
			return false;
		if (prefix.equals(baseUnit))
			return true;
		
		for (int i = metricUnits.length - 1; i >= 0; --i) 
			if (prefix.equals(metricUnits[i]))
				return true;
		
		return false;
	}
	
	public static String getPrefix(String fullUnit, String baseUnit) {
		// Check size
		if (baseUnit == null || baseUnit.isEmpty() || fullUnit == null || fullUnit.isEmpty())
			return null;
		// Check if base unit is even in the convert to unit
		int index = fullUnit.lastIndexOf(baseUnit);
		if (index == -1)
			return null;
		else if (index == 0 && fullUnit.equals(baseUnit))
			return baseUnit;
		
		return fullUnit.substring(0, index);
	}
	
	public static int getPowerForPrefix(String prefix) {
		int i = 0;
		for (; i < metricUnits.length; ++i)
			if (prefix.equals(metricUnits[i]))
				break;
		
		if (i >= metricUnits.length)
			return 0;
		
		i -= i < 6 ? 6 : 5;
		
		return i;
	}
	
	public static void main(String[] s) {
		convert("p", "T", 1);
		convert("", "da", 100000);
		convert("", "T", 1);
	}
	
	private static void convert(String from, String to, int value) {
		System.out.println(value + from + " = " +  value * Math.pow(10, getPowerForConversion(from, to))+  to);
	}
	
	public static int getPowerForConversion(String from, String to) {
		return getPowerForPrefix(to) - getPowerForPrefix(from);
	}

	public static double getConversionFactor(String from, String to) {
		return Math.pow(10, getPowerForConversion(from, to));
	}
}
