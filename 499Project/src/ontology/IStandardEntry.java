package ontology;

import java.util.ArrayList;

import s3filecontrol.HeaderValueTuple;

public interface IStandardEntry {
	IStandardEntryConverter getConverter(HeaderValueTuple tuple);
	IStandardEntryConverter getConverter(HeaderValueTuple tuple, int index);
	String getStandardHeader();
	void addVariation(String variation);
	ArrayList<String> getVariations();
}
