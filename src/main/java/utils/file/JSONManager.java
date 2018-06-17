package utils.file;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.jayway.jsonpath.JsonPath;

import groovy.json.JsonException;

public class JSONManager {
	
	public synchronized static Object getValueFromJson(String jsonPath, String json) {
		return JsonPath.parse(json).read(jsonPath);
	}

	public synchronized static <T> Object getValueOfTypeFromJson(String jsonPath, Class<T> type, String json) {
		return JsonPath.parse(json).read(jsonPath, type);
	}
	
	public static Map<String, Object> convertJsonDataToMap(String jsonFilePath) {
		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader(jsonFilePath));

			JSONObject jsonObject = (JSONObject) obj;
//			System.out.println("json object is: " + jsonObject);
			Map<String, Object> conMap = new HashMap<>();

			if (jsonObject != null) {
				//convert the data from Json to store to map
				conMap = toMap(jsonObject);
//				System.out.println("aftet converting to map:" + conMap);
				return conMap;
			}

			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, Object> toMap(JSONObject object) throws JsonException {
		Map<String, Object> map = new HashMap<>();

		Iterator<String> keysItr = object.keySet().iterator();
		while (keysItr.hasNext()) {
			//get the key, value pair of the json data
			String key = keysItr.next();
			Object value = object.get(key);

			//for value is an array, iterator the array
			if (value instanceof JSONArray) {
				value = toMap((JSONArray) value);
			}

			//for value is an object, iterator the array
			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}
	
	private static List<Object> toMap(JSONArray array) throws JsonException {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toMap((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}
	
	//to write agent email and password to the json file	
	public static void writeLogin(String email, String password, String jsonPath){
		JSONObject obj = new JSONObject();
		obj.put("email", email);
		obj.put("password", password);

		try {

			FileWriter file = new FileWriter(jsonPath);
			file.write(obj.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

//		System.out.print(obj);

	}
	
	public static void main(String[] args){
		String jsonFilePath = "data/test.json";
//		Map<String, Object> agentLoginData = JSONManager.convertJsonDataToMap(jsonFilePath);
//		List<String> email =  (List<String>) agentLoginData.get("email");
//		String pwd = (String) agentLoginData.get("password");
//		System.out.println(email);
//		System.out.println(pwd);
		String email = "indecorumagenttestar0001@123.com";
				String password = "password";
				writeLogin(email,password, jsonFilePath);
		
	}
}

