/*
 * Implements the Converter interface
 * Takes a CSV file as a string and returns the corresponding JSON array 
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CsvConverter implements Converter {

	public boolean canConvertExtension(String extension) {
		return extension.equalsIgnoreCase(".csv");
	}

	public JSONArray convertToJSON(String fileText) {
		String header = getHeader(fileText);

		// No header found
		if (header == null) {
			return null;
		}

		String[] headerFields = splitLine(header);
		int headerCount = headerFields.length;
		// Header contains no values
		if (headerCount == 0) {
			return null;
		}

		String[] lines = splitLines(fileText);
		JSONArray jsonArray = new JSONArray();
		JSONObject object;

		try {
			for (int i = 1; i < lines.length; i++) {

				object = new JSONObject();
				String[] lineFields = splitLine(lines[i]);

				for (int j = 0; j < headerCount; j++) {
					object.put(headerFields[j], lineFields[j]);
				}

				jsonArray.add(object);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return jsonArray;
	}

	private String[] splitLine(String line) {
		return line.split(",");
	}

	private String[] splitLines(String fileText) {
		return fileText.split("\n");
	}

	private String getHeader(String fileText) {
		String[] header = fileText.split("\n");
		return header.length > 0 ? header[0] : null;
	}

}
