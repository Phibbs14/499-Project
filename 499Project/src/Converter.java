import org.json.simple.JSONArray;


public interface Converter {
	boolean canHandlerExtension(String extension);
	JSONArray convertToJSON(String fileText);
}
