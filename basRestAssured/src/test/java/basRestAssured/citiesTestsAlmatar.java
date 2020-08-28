package basRestAssured;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import gherkin.deps.com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import utility.genericFunctions;

import io.restassured.specification.ProxySpecification;





public class citiesTestsAlmatar {
	
	public static Response response;
	public static RequestSpecification httpRequest;
	public JsonPath jsonPathEvaluator;
	@SuppressWarnings("rawtypes")
	public ResponseBody responseJSON;
	public static String responseAsString;
	public String itineraryId;
	public String cartId;
	public String searchRequestId;
	public String userEmail="aa@bb.com";
	public String password = "12345678";
		
	
	//-----------------Pre-search EndPoints----------------------------------------------
	public String citiesEndPoint = "/api/v2/user-recommendation/flights/cities";
	public String destinationsEndPoint = "/api/v2/user-recommendation/destinations";
	public String offersEndPoint = "/api/v2/user-recommendation/offers";
	public String ratesEndPoint = "/api/v1/dictionary/currency/rates";
	public String topDestinationsEndPoint = "/api/v2/user-recommendation/airlines/top";
	public String suggestionsEndPoint = "/api/v2/suggestion/airsuggest?q=DMM";
	
	
	//----------------Search End Point--------------------------------------------------
	public String searchEndPoint = "/api/v1/flights-consumer/flights/search";
	
	
	//-----------------Post Search End points-------------------------------------------
	public String validateItenaryEndPoint = "/api/v1/flights-consumer/flights/validate/";
	public String itineraryDetailsEndPoint = "/api/v1/flights-consumer/flights/details/";
	public String phoneCodesEndPoint = "/api/v1/country/phone";
	//-----
	public String loginEndpoint = "/api/v1/user-profile/login";
	public String userProfileEndPoint ="/api/v1/user-profile/profile/info";
	public String userProfileHistoryEndPoint  = "/api/v1/user-profile/history";
	public String userProfileIdentities  = "/api/v1/user-profile/profile/identities";
	//------
	public String createBookiing ="/api/v2/flights-consumer/book";
	
	
	
	public String createBookingEndPoint = "/api/v2/flights-consumer/book";
	public String cartDetailsEndPoint = "/api/v1/flights-consumer/booking/cart/"+cartId;
	
	//---------------- The nodes within the response body------------------
	public String statusField;
	public JsonObject[] dataNode;
	public String[] data_CityNamesField;
	public String[] data_CountryNamesField;
	public String data_CountryCodeField;
	public JsonObject[] destinationsNode;
	public String destinations_cityNameField;
	public String destinations_countryNameField;
	public String destinations_countryCodeField;
	public String destinations_imageField;
	public String destinations_noteField;
	public String[] errorsNode;
	public float itineraryPrice=0;
	public boolean isItineraryAvailale=true;
	public boolean isPriceChanged=false;
	public int statusCode =200;
	public int availableItineraryCount=0;
	public List<String> availableItineraries;
	public List<String> availableItinerariesIDs;
	int randomindex=0;
	
	
	
	@Test(priority = 9,enabled=true)
	public void getItineraryDetails() throws IOException
	{
		if(isItineraryAvailale!=true)
		{
			throw new SkipException("Skipping this exception");
		}
		
		response=httpRequest.log().all().get(itineraryDetailsEndPoint+itineraryId);
		responseJSON = response.body();
		responseAsString = responseJSON.asString();
		jsonPathEvaluator = response.jsonPath();
		//System.out.println(responseAsString);
		int statusCode= response.getStatusCode();
		float priceInItineraryDetails = jsonPathEvaluator.get("data.IteneraryTotalFareAmount");
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		Assert.assertEquals(priceInItineraryDetails, itineraryPrice , "Price is correct.");
	}
	
	@Test(priority = 8,enabled=true)
	public void validateItinerary() throws IOException
	{
		for (int i=98;i<=availableItineraryCount;i++)
		{
			 try
			 {
				    response=httpRequest.log().all().get(validateItenaryEndPoint+itineraryId);
					responseJSON = response.body();
					responseAsString = responseJSON.asString();
					jsonPathEvaluator = response.jsonPath();
					System.out.println(responseAsString);
					statusCode= response.getStatusCode();
					
					boolean responseValid=jsonPathEvaluator.getList("").contains("data");
					
					System.out.println(responseValid);
					
					isItineraryAvailale = jsonPathEvaluator.get("data.itineraryAvailable");
					isPriceChanged = jsonPathEvaluator.get("data.priceChanged");
					
					
					if(isItineraryAvailale==true&&isPriceChanged==false)
					{
						break;
						
					}
					
					else if(i==availableItineraryCount)
					{
						
						searchOneWayItinerary();
					}
					
					else
					{
						//searchOneWayItinerary();
						randomindex = genericFunctions.generateRandomNumber(availableItineraryCount);
						System.out.println(randomindex);
						
						String newItineraryId = availableItinerariesIDs.toArray()[randomindex].toString();
						itineraryId = newItineraryId;
					    System.out.println(itineraryId);
						
						System.out.println("The itinerary is not availay any longer.");
					}
			 }
			 
			 catch(Exception e)
			 {
				    randomindex = genericFunctions.generateRandomNumber(availableItineraryCount);
					System.out.println(randomindex);
					
					String newItineraryId = availableItinerariesIDs.toArray()[randomindex].toString();
					itineraryId = newItineraryId;
				    System.out.println(itineraryId);
					
					System.out.println("The itinerary is not availay any longer.");
				 
			 }
		
			
						
		}
		

		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		Assert.assertEquals(isItineraryAvailale , true , "Response code is ok.");
		
		
	}


	@Test(priority = 7,enabled=true,retryAnalyzer = TestRetryAnalyzer.class)
	public void searchOneWayItinerary() throws IOException
	{
		//Payload external json file
		String requestBody = genericFunctions.generateStringFromResource("src\\test\\resources\\flightSearchCriteria.json");
		response=httpRequest.contentType("application/json").body(requestBody).log().all().post(searchEndPoint);
		responseJSON = response.body();
		responseAsString = responseJSON.asString();
		jsonPathEvaluator = response.jsonPath();
		statusCode= response.getStatusCode();
		jsonPathEvaluator = response.jsonPath();
		
		availableItineraryCount=jsonPathEvaluator.getList("data.Itenerary").size();
		System.out.println(availableItineraryCount);
		
		availableItinerariesIDs = jsonPathEvaluator.getList("data.Itenerary.IteneraryID");
		System.out.println(availableItinerariesIDs);
		
		
		randomindex = genericFunctions.generateRandomNumber(availableItineraryCount);
		System.out.println(randomindex);
		
		
		itineraryId = jsonPathEvaluator.get("data.Itenerary["+randomindex+"].IteneraryID");
		System.out.println(itineraryId);
		
		
		itineraryPrice = jsonPathEvaluator.get("data.Itenerary["+randomindex+"].IteneraryTotalFareAmount");
		System.out.println(itineraryPrice);
			
		
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		
	
	}
	
	
	
	@BeforeClass
	public void setUp()
	{
		//RestAssured.authentication = RestAssured.basic("ahmed.abokhalifa", "12345678");
		RestAssured.proxy("192.168.1.5", 8888);
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI = "https://alpha.alm6ar.com";
		 
		 httpRequest = RestAssured.given();
		 LogConfig logconfig = new LogConfig().enableLoggingOfRequestAndResponseIfValidationFails().enablePrettyPrinting(true);
		 RestAssured.config().logConfig(logconfig);
		 
	 
	}
	
	@Test(priority = 1,enabled=false)
	public void requestCities()
	{
		response = httpRequest.get(citiesEndPoint);
		responseJSON = response.body();
		responseAsString = responseJSON.asString();
		System.out.println(responseAsString);
        jsonPathEvaluator = response.jsonPath();
		int statusCode= response.getStatusCode();
		
		int status1 = jsonPathEvaluator.get("status");
		//int cityCount = jsonPathEvaluator.getList("data").size();
		System.out.println(status1);
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		
	}
	
	@Test(priority = 2,enabled=false)
	public void requestDetinations()
	{
		response = httpRequest.get(destinationsEndPoint);
		responseJSON = response.body();
		responseAsString = responseJSON.asString();
		System.out.println(responseAsString);
		int statusCode= response.getStatusCode();
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		
	}
	
	@Test(priority = 3,enabled=false)
	public void requestOffers()
	{
		response = httpRequest.get(offersEndPoint);
		responseJSON = response.body();
		responseAsString = responseJSON.asString();
		System.out.println(responseAsString);
		int statusCode= response.getStatusCode();
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		
	}
	
	@Test(priority = 4,enabled=false)
	public void requestRates()
	{
		response = httpRequest.get(ratesEndPoint);
		responseJSON = response.body();
		responseAsString = responseJSON.asString();
		System.out.println(responseAsString);
		int statusCode= response.getStatusCode();
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		
	}
	
	@Test(priority = 5,enabled=false)
	public void requestTopDestinations()
	{
		response = httpRequest.get(topDestinationsEndPoint);
		responseJSON = response.body();
		responseAsString = responseJSON.asString();
		System.out.println(responseAsString);
		int statusCode= response.getStatusCode();
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		
	}
	
	@Test(priority = 6,enabled=false)
	public void requestSuggestions()
	{
		response = httpRequest.get(suggestionsEndPoint);
		responseJSON = response.body();
		responseAsString = responseJSON.asString();
		System.out.println(responseAsString);
		int statusCode= response.getStatusCode();
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
	
		
	}
	
	
	@Test(priority = 700,enabled=false,retryAnalyzer = TestRetryAnalyzer.class)
	public void searchOneWayItineraryDEMO() throws IOException
	{
		//Payload external json file
		String requestBody = genericFunctions.generateStringFromResource("src\\test\\resources\\flightSearchCriteria.json");
		response=httpRequest.contentType("application/json").body(requestBody).log().all().post(searchEndPoint);
		responseJSON = response.body();
		responseAsString = responseJSON.asString();
		jsonPathEvaluator = response.jsonPath();
		statusCode= response.getStatusCode();
		jsonPathEvaluator = response.jsonPath();
		
		availableItineraryCount=jsonPathEvaluator.getList("data.Itenerary").size();
		System.out.println(availableItineraryCount);
		
		availableItineraries = jsonPathEvaluator.getList("data.Itenerary.IteneraryID");
		System.out.println(availableItineraries);
		
		
		int randomindex = genericFunctions.generateRandomNumber(availableItineraryCount);
		System.out.println(randomindex);
		
		
		itineraryId = jsonPathEvaluator.get("data.Itenerary["+randomindex+"].IteneraryID");
		System.out.println(itineraryId);
		
		
		itineraryPrice = jsonPathEvaluator.get("data.Itenerary["+randomindex+"].IteneraryTotalFareAmount");
		System.out.println(itineraryPrice);
		
		
		//Dealing with arrays and lists
		System.out.println(Arrays.toString(availableItineraries.toArray()));
		
		//for(int i = 1 ; i<itineraryCount;i++ )
	   // { 
	        //System.out.println(availableItineraries.get(i).toString());
	      //  System.out.println(availableItineraries.toArray()[i].toString());
	    //}
		
		for(String itineraryID : availableItineraries)
		 {
		 System.out.println("ItineraryId: " + itineraryID);
		 }
		
		Assert.assertEquals(statusCode , 200 , "Response code is ok.");
		
	
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	

}
