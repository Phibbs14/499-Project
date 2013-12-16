package ontology;

import s3filecontrol.HeaderValueTuple;

public interface IOntology {
	IStandardEntry getStandardEntryFor(HeaderValueTuple tuple);
	boolean outputToFile(String filename);
	boolean readFromFile(String filename);
}
