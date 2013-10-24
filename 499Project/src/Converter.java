import org.json.simple.JSONArray;


public interface Converter {
	boolean canConvertExtension(String extension);
	JSONArray convertToJSON(String fileText);
}
