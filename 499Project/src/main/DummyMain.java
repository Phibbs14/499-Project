package main;
import java.io.IOException;

import logging.ILogger;
import logging.StringLogger;
import parser.IParserFactory;
import parser.ParserFactory;
import s3filecontrol.DataFile;
import s3filecontrol.IFileManager;
import s3filecontrol.S3FileManager;
import fileconverters.IFileFormatConverter;
import fileconverters.MainConverter;

//import static org.elasticsearch.node.NodeBuilder.*;


public class DummyMain {
	
	public static String test2() {
		return "Test Return";
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(test());
		/*
		try {
			ILogger logger = new ConsoleLogger();
			
			logger.logEvent("Session starting");
			
			IFileManager m = new S3FileManager(logger);
			IFileFormatConverter converter = new MainConverter(logger);
			IParserFactory parserFactory = new ParserFactory(logger);
			if (!parserFactory.initializeComponents()) {
				logger.logError("The parser factory failed to initialize, cannot recover.");
				return;
			}
				
			
			m.pullCurrentFileList();
			DataFile file = null;
			
			while ((file = m.getFileToProcess()) != null)
			{
				converter.convert(file);
				if (!file.isFileConverted())
					logger.logEvent("Failed to convert " + file.getFileKey());
				else {
					logger.logEvent("Sucessfully converted file " + file.getFileKey());
					logger.logEvent(file.getConvertedJsonArray().toJSONString().substring(0, 100));
					logger.logEvent("Starting file standardization");
					if (parserFactory.parseFile(file))
						logger.logEvent("Standardization complete: " + file.getStandardJsonArray().toJSONString().substring(0, 100));
					else
						logger.logEvent("Standardization failed.");
				}
				file.setProcessed();
			}
			
			logger.logEvent("Session Complete");
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}*/
	}
	
	public static String test() {
		ILogger logger = null;
		try {
			logger = new StringLogger();
			
			logger.logEvent("Session starting");
			
			IFileManager m = new S3FileManager(logger);
			IFileFormatConverter converter = new MainConverter(logger);
			IParserFactory parserFactory = new ParserFactory(logger);
			if (!parserFactory.initializeComponents()) {
				logger.logError("The parser factory failed to initialize, cannot recover.");
				return logger.toString();
			}
				
			
			m.pullCurrentFileList();
			DataFile file = null;
			
			while ((file = m.getFileToProcess()) != null)
			{
				converter.convert(file);
				if (!file.isFileConverted())
					logger.logEvent("Failed to convert " + file.getFileKey());
				else {
					logger.logEvent("Sucessfully converted file " + file.getFileKey());
					logger.logEvent(file.getConvertedJsonArray().toJSONString().substring(0, 100));
					logger.logEvent("Starting file standardization");
					if (parserFactory.parseFile(file))
						logger.logEvent("Standardization complete: " + file.getStandardJsonArray().toJSONString().substring(0, 100));
					else
						logger.logEvent("Standardization failed.");
				}
				file.setProcessed();
			}
			
			logger.logEvent("Session Complete");
			
			
			
		} catch (Exception ex) {
			return "error : " + ex.getMessage();
		}
		return logger == null ? "Logger failed to created" : logger.toString();
	}
}
