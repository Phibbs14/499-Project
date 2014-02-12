package main;
import java.io.IOException;

import logging.ConsoleLogger;
import logging.ILogger;
import parser.IParserFactory;
import parser.ParserFactory;
import s3filecontrol.DataFile;
import s3filecontrol.IFileManager;
import s3filecontrol.S3FileManager;
import fileconverters.IFileFormatConverter;
import fileconverters.MainConverter;


public class DummyMain {
	public static void main(String[] args) throws IOException {
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
		}
	}
	
	private static void fileTransferTest() {
		System.out.println("begin!");
		
		IFileManager m = new S3FileManager(new ConsoleLogger());
		
		m.pullCurrentFileList();
		DataFile file = null;
		while ((file = m.getFileToProcess()) != null)
		{
			file.setProcessing();
			System.out.println(file.getFileLocation());
			
			file.setProcessed();
			System.out.println(file.getFileLocation());
			
			file.setFailed();
			System.out.println(file.getFileLocation());
			
			file.setUnprocessed();
			System.out.println(file.getFileLocation());
		}
		
		System.out.println("Done");
	}
}
