package ontology;

public interface IOntology {
	IStandardEntry getStandardEntryFor(String header, String value);
	void outputToFile(String filename);
	void readFromFile(String filename);
}
