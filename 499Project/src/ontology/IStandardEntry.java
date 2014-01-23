package ontology;

import s3filecontrol.HeaderValueTuple;

public interface IStandardEntry {
	IStandardEntryConverter getConverter(HeaderValueTuple tuple);
	IStandardEntryConverter getConverter(HeaderValueTuple tuple, int index);
}
