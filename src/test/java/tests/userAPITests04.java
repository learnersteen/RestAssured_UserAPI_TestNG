//package tests;
//
//import java.util.List;
//import java.util.Map;
//
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import api.BaseTest;
//
//
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import pojo.UserDetails;
//import utils.ApiHelper;
//import utils.TestDataProvider;
//import utils.UserResponseValidator;
//import utils.validateResponseData;
//
//
//public class userAPITests04 extends BaseTest {
//	
//	@Test(dataProvider = "postUserData", dataProviderClass = TestDataProvider.class, priority = 1)
//	public void testCreateUser(Map<String, Object> testCase) throws Exception {
//	    Response postResponse = ApiHelper.sendPostRequest(testCase);
//
//	    //UserResponseValidator.validatePostResponse(postResponse, ApiHelper.buildUserFromMap(testCase), testCase);
//	    
//	   
//	}
//	
//	@Test(dataProvider = "putUserData", dataProviderClass = TestDataProvider.class, priority = 2)
//	public void testUpdateUser(Map<String, Object> testCase) throws Exception {
//	    Response putResponse = ApiHelper.sendPutRequest(testCase);
//
//	    // Add validation of PUT response here
//	    //UserResponseValidator.validatePutResponse(putResponse, ApiHelper.buildUserFromMap(testCase), testCase);
//	}
//	
//	@Test(dataProvider = "patchUserData", dataProviderClass = TestDataProvider.class, priority = 3)
//	public void testUpdateUserPatch(Map<String, Object> testCase) throws Exception {
//	    // Send PATCH request and get response
//	    Response patchResponse = ApiHelper.sendPatchRequest(testCase);
//
//	    // Deserialize actual response JSON to UserDetails POJO
//	    ObjectMapper mapper = new ObjectMapper();
//	    UserDetails actualUser = mapper.readValue(patchResponse.getBody().asString(), UserDetails.class);
//
//	    // Build expected UserDetails from test data (your Map)
//	    UserDetails expectedUser = ApiHelper.buildUserFromMap(testCase);
//
//	    // Set userId explicitly in expectedUser from actual response (since patchResponse includes it)
//	    expectedUser.setUserId(actualUser.getUserId());
//
//	    // Optional: You can ignore or set dummy values for creationTime and lastModTime if needed
//	    // expectedUser.setCreationTime(actualUser.getCreationTime());
//	    // expectedUser.setLastModTime(actualUser.getLastModTime());
//
//	    // Now validate actual vs expected response using the validator utility
//	   // UserResponseValidator.validateUserDetails(actualUser, expectedUser);
//
//	    // Also, assert HTTP status code 200 for success
//	    Assert.assertEquals(patchResponse.getStatusCode(), 200, "PATCH request failed");
//	}
//
//	@Test(dataProvider = "getUserByIdData", dataProviderClass = TestDataProvider.class, priority = 4)
//	public void testGetUserById(Map<String, Object> testCase) throws Exception {
//		
//		 // Send GET request and get response
//	    Response getResponse = ApiHelper.sendGetUserByIdRequest(testCase);
//	    
//	 // Assert HTTP status code matches expected
//	    int expectedStatusCode = (int) testCase.getOrDefault("expectedStatusCode", 200);
//	    Assert.assertEquals(getResponse.getStatusCode(), expectedStatusCode, "GET request failed");
//
//	    // Deserialize actual response JSON to UserDetails POJO
//	    ObjectMapper mapper = new ObjectMapper();
//	    UserDetails actualUser = mapper.readValue(getResponse.getBody().asString(), UserDetails.class);
//
//	    // Build expected UserDetails from test data map (for validation)
//	    UserDetails expectedUser = ApiHelper.buildUserFromMap(testCase);
//
//	    // Set userId explicitly in expectedUser (since you expect that userId is returned)
//	    expectedUser.setUserId(ApiHelper.getUserId());
//	    System.out.println("GET User by ID Test passed for userId: " + ApiHelper.getUserId());
//	}
//	
//	@Test(dataProvider = "DeleteUserByIdData", dataProviderClass = TestDataProvider.class, priority = 5)
//	public void testDeleteUserById(Map<String, Object> testCase) throws Exception {
//
//	    // Send DELETE request and get response
//	    Response deleteResponse = ApiHelper.sendDeleteUserByIdRequest(testCase);
//
//	    // Assert HTTP status code matches expected
//	    int expectedStatusCode = (int) testCase.getOrDefault("expectedStatusCode", 200);
//	    Assert.assertEquals(deleteResponse.getStatusCode(), expectedStatusCode, "DELETE request failed");
//
//	    // Optionally, validate response body if your API returns anything useful on DELETE
//	    System.out.println("DELETE Response Body: " + deleteResponse.getBody().asPrettyString());
//
//	    System.out.println("DELETE User by ID Test passed for userId: " + ApiHelper.getUserId());
//
//	    // Clear stored userId and firstname after delete 
//	    ApiHelper.setUserId(null);
//	    ApiHelper.setUserFirstname(null);
//	}
//	
//	@Test(dataProvider = "getAllUsersData", dataProviderClass = TestDataProvider.class, priority = 6)
//	public void testGetAllUsers(Map<String, Object> testCase) throws Exception {
//	    String endpoint = (String) testCase.get("endpoint");
//
//	    Response response = ApiHelper.sendGetAllUsersRequest(endpoint);
//
//	    int expectedStatusCode = (int) testCase.getOrDefault("expectedStatusCode", 200);
//	    Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "GET All Users request failed");
//
//	    // You can deserialize the response into a List of UserDetails if your API returns a JSON array
//	    ObjectMapper mapper = new ObjectMapper();
//	    List<UserDetails> users = mapper.readValue(response.getBody().asString(),
//	        mapper.getTypeFactory().constructCollectionType(List.class, UserDetails.class));
//
//	    System.out.println("Total users fetched: " + users.size());
//
//	    // Optionally add asserts on the list size or specific user data
//
//	    Assert.assertTrue(users.size() > 0, "No users found in GET All Users response");
//	}
//
//	
//
//	
//
//}
//    
//
