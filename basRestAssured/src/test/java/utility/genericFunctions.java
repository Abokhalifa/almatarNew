package utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

//import gherkin.deps.com.google.gson.JsonObject;
//import net.minidev.json.JSONObject;
//import net.minidev.json.JSONValue;

public class genericFunctions {
	public static String generateStringFromResource(String path) throws IOException {

	    return new String(Files.readAllBytes(Paths.get(path)));

	}
	
	public static int generateRandomNumber(int upperBound) throws IOException {

		Random rand = new Random();
		int randomNumber = rand.nextInt(upperBound); 
	    return randomNumber;

	}
	
	
	
//	public static JsonObject convertStringToJSON(String body) {
//		
//		JsonObject data = (JSONObject)JSONValue.parse(body);
//		return data;
//		
//	}

}
