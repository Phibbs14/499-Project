package ontology;

import s3filecontrol.HeaderValueTuple;

public interface IStandardEntryConverter {
	HeaderValueTuple convert(HeaderValueTuple orginal);
}
