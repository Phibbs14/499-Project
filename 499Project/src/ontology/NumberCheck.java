package ontology;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberCheck {

	// Returns true if the string contains letters and numbers
	public static boolean containsNumber(String str) {
		return str.matches("[a-z, A-Z]+[0-9]+");
	}
	
	// Return the first set of characters before anything else appears
	public static String getHeaderWithoutNumber(String str) {
		Pattern p1 = Pattern.compile("[a-z, A-Z]+");
		Matcher m = p1.matcher(str);
		
		if (m.find())
			return m.group(0).trim();
		return null;
	}
	
	// Returns the first number contained in the string
	public static int getNumber(String str) {
		Pattern p1 = Pattern.compile("[0-9]+");
		Matcher m = p1.matcher(str);
		
		if (m.find()) {
			try {
			return Integer.parseInt(m.group(0));
			} catch (Exception ex) {
				return -1;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) throws IOException {
		String[] sa = {"Voltage 1", "V1", "V3"};
		
		for (String s : sa) 
			System.out.println(getHeaderWithoutNumber(s) + "," + getNumber(s));
	}
}

