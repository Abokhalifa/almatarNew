package basRestAssured;
import io.restassured.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matcher.*;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;




public class functionalTests {
	
	public static Response response;
	@SuppressWarnings("rawtypes")
	public ResponseBody responseJSON;
	public static String responseAsString;
	public int countryCode = 90210;
	public JsonPath jsonPathEvaluator;

	
	@BeforeTest
	public void sendRequestAndStoreResponse()
	{
		 RestAssured.baseURI = "https://zippopotam.us/us";
		 RequestSpecification httpRequest = RestAssured.given();
		 response = httpRequest.get("/"+countryCode);
		 responseJSON = response.getBody();
		 responseAsString = responseJSON.asString();
		 System.out.println(responseAsString);	
		 jsonPathEvaluator = response.jsonPath();
	}
	
	public String getValueOfNode(String nodeName)
	{		
		String nodeValue = jsonPathEvaluator.get(nodeName);
		return nodeValue;
	}
	
	public int getSizeOfNode(String nodeName)
	{
		int nodeSize = jsonPathEvaluator.getList(nodeName).size();
		return nodeSize;
	}
	
	@Test(priority =1)
	public void validateStatusCode()
	{
		int statusCode= response.getStatusCode();
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		
	}
	
	@Test(priority =2)
	public void validateCountryName()
	{
		String countryName = getValueOfNode("country"); 
		Assert.assertEquals(countryName,"United States");
		
	}
	
	@Test(priority =3)
	public void validateCountryPostCode()
	{
		String countryPostCode = getValueOfNode("'post code'"); 
		Assert.assertEquals(countryPostCode,"90210");
		
	}
	
	@Test(priority =4)
	public void validateCountryAbbreviation()
	{
		String countryPostCode = getValueOfNode("'country abbreviation'"); 
		Assert.assertEquals(countryPostCode,"US");
		
	}
	
	@Test(priority =5)
	public void validateCountOfPlaces()
	{
		int countOfPlaces = getSizeOfNode("places");
		Assert.assertEquals(countOfPlaces,1);
		
	}
	
	@Test(priority =6)
	public void validatePlaceName()
	{
		String placeName = getValueOfNode("places[0].'place name'"); 
		Assert.assertEquals(placeName,"Beverly Hills");
		
	}
	
	@Test(priority =7)
	public void validateLongitude()
	{
		String logitude = getValueOfNode("places[0].longitude"); 
		Assert.assertEquals(logitude,"-118.4065");
		
	}
	@Test(priority =8)
	public void validateState()
	{
		String state = getValueOfNode("places[0].state"); 
		Assert.assertEquals(state,"California");
		
	}
	
	@Test(priority =9)
	public void validateStateAbbreviation()
	{
		String stateAbbreviation = getValueOfNode("places[0].'state abbreviation'"); 
		Assert.assertEquals(stateAbbreviation,"CA");
		
	}
	
	@Test(priority =10)
	public void validateLatitude()
	{
		String latitude = getValueOfNode("places[0].latitude"); 
		Assert.assertEquals(latitude,"34.0901");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	

}
