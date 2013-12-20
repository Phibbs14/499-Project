package s3filecontrol;

public class HeaderValueTuple {
	private String header;
	private String value;
	private Double valueAsDouble = null;

	public HeaderValueTuple(String h, String v) {
		header = h;
		value = v;
	}

	public String getHeader() {
		return header;
	}

	public String getValue() {
		return value;
	}

	public double getValueAsDouble() {
		return isValueANumber()? valueAsDouble : 0; // throw exception if not?
	}
	
	public boolean isValueANumber() {
		if (valueAsDouble != null)
			return true;
		try {
			valueAsDouble = Double.parseDouble(value);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		return "H: " + header + ", V:" + value;
	}
}
