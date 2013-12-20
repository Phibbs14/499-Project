package parser;

import s3filecontrol.IParsingFile;

public interface IParserFactory {
	boolean parseFile(IParsingFile dataFile);

	boolean initializeComponents();
}
