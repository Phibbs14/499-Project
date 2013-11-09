/*
 * Implements the Converter interface
 * Takes a XML file as a string and returns the corresponding JSON array 
 */

import java.io.File;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class XmlConverter implements Converter{

	public boolean canConvertExtension(String extension) {
		return extension.equalsIgnoreCase(".xml");
	}

	public JSONArray convertToJSON(String fileText) {
		JSONArray jsonArray = new JSONArray();
		JSONObject object;
		
		SAXBuilder builder = new SAXBuilder();
		
		Reader xmlStringInput = new StringReader(fileText.trim());
		
		try {
			object = new JSONObject();
			Document document = builder.build(xmlStringInput);
			Element rootNode = document.getRootElement();
			List sensorDataList = rootNode.getChildren();
			
			//Loop through the second layer of nodes of the xml document
			for (int i = 0; i < sensorDataList.size(); i++) {
				Element individualSensorData = (Element) sensorDataList.get(i);
				List individualSensorDataList = individualSensorData.getChildren();
				
				//Go through each sensor data individual values
				for (int j = 0; j < individualSensorDataList.size(); j++) {
					Element item = (Element) individualSensorDataList.get(j);
					object.put(item.getName(), item.getValue());
				}
				jsonArray.add(object);
			}
			
			//System.out.println(jsonArray.toJSONString());
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return jsonArray;
	}

}
