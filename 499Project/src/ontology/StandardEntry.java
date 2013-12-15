package ontology;

public class StandardEntry implements IStandardEntry {

	public IStandardEntryConverter getConverter(String header) {
		// This can return itself, or a metric converter if it needs to convert the value
		// example if header = kV, we might want to convert to V
		// but for Volt, Voltage, or Volts, we can just return this
		
		// TODO Auto-generated method stub
		return null;
	}

}
