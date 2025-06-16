package tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import api.BaseTest;

import io.restassured.response.Response;

import pojo.UserDetails;
import utils.ApiHelper;
import utils.TestDataProvider;

import utils.Validator;



public class UserAPITests extends BaseTest {
	
	
	@Test(dataProvider = "postUserData", dataProviderClass = TestDataProvider.class, priority = 1)
	public void testCreateUser(Map<String, Object> testCase) throws Exception {
	    Response postResponse = ApiHelper.sendPostRequest(testCase);
	    Assert.assertEquals(postResponse.getStatusCode(), 201);

	    ObjectMapper mapper = new ObjectMapper();
	    UserDetails actualUser = mapper.readValue(postResponse.getBody().asString(), UserDetails.class);

	    UserDetails expectedUser = ApiHelper.buildUserFromMap(testCase);

	    Validator validator = new Validator();
	    validator.validateAllFields(actualUser, expectedUser); // This matches new method signature
	}


	
	@Test(dataProvider = "putUserData", dataProviderClass = TestDataProvider.class, priority = 2)
	public void testUpdateUser(Map<String, Object> testCase) throws Exception {
	    Response putResponse = ApiHelper.sendPutRequest(testCase);
	    Assert.assertEquals(putResponse.getStatusCode(), 200);

	    ObjectMapper mapper = new ObjectMapper();
	    UserDetails actualUser = mapper.readValue(putResponse.getBody().asString(), UserDetails.class);

	    UserDetails expectedUser = ApiHelper.buildUserFromMap(testCase);

	    Validator validator = new Validator();
	    validator.validateAllFields(actualUser, expectedUser); // This matches new method signature
	}

	@Test(dataProvider = "patchUserData", dataProviderClass = TestDataProvider.class, priority = 3)
	public void testUpdateUserPatch(Map<String, Object> testCase) throws Exception {
	    // Send PATCH request and get response
	    Response patchResponse = ApiHelper.sendPatchRequest(testCase);

	    Assert.assertEquals(patchResponse.getStatusCode(), 200);

	  
	}

	@Test(dataProvider = "getUserByIdData", dataProviderClass = TestDataProvider.class, priority = 4)
	public void testGetUserById(Map<String, Object> testCase) throws Exception {
		
		 // Send GET request and get response
	    Response getResponse = ApiHelper.sendGetUserByIdRequest(testCase);
	    
	 // Assert HTTP status code matches expected
	    int expectedStatusCode = (int) testCase.getOrDefault("expectedStatusCode", 200);
	    Assert.assertEquals(getResponse.getStatusCode(), expectedStatusCode, "GET request failed");

	    // Deserialize actual response JSON to UserDetails POJO
	    ObjectMapper mapper = new ObjectMapper();
	    UserDetails actualUser = mapper.readValue(getResponse.getBody().asString(), UserDetails.class);
	    

	    UserDetails expectedUser = ApiHelper.buildUserFromMap(testCase);

//	    Validator validator = new Validator();
//	    validator.validateAllFields(actualUser, expectedUser);

	    // Set userId explicitly in expectedUser 
	    expectedUser.setUserId(ApiHelper.getUserId());
	    System.out.println("GET User by ID Test passed for userId: " + ApiHelper.getUserId());
	}

	
	@Test(dataProvider = "DeleteUserByIdData", dataProviderClass = TestDataProvider.class,  dependsOnMethods = {"testCreateUser"}, priority = 7)
     public void testDeleteUserById(Map<String, Object> testCase) throws Exception {

	    // Send DELETE request and get response
	    Response deleteResponse = ApiHelper.sendDeleteUserByIdRequest(testCase);

	    // Assert HTTP status code matches expected
	    int expectedStatusCode = (int) testCase.getOrDefault("expectedStatusCode", 200);
	    Assert.assertEquals(deleteResponse.getStatusCode(), expectedStatusCode, "DELETE request failed");

	    // Optionally, validate response body if your API returns anything useful on DELETE
	    System.out.println("DELETE Response Body: " + deleteResponse.getBody().asPrettyString());

	    System.out.println("DELETE User by ID Test passed for userId: " + ApiHelper.getUserId());

	    // Clear stored userId and firstname after delete 
	  //  ApiHelper.setUserId(null);
	   // ApiHelper.setUserFirstname(null);
	}

	
	@Test(dataProvider = "GetAllUsersData", dataProviderClass = TestDataProvider.class, priority = 6)
	public void testGetAllUsers(Map<String, Object> testCase) throws Exception {
	    String endpoint = (String) testCase.get("endpoint");

	    Response response = ApiHelper.sendGetAllUsersRequest(endpoint);

	    // Assert status code
	    int expectedStatusCode = (int) testCase.getOrDefault("expectedStatusCode", 200);
	    Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "GET All Users request failed");

	    // Validate Content-Type header is JSON
	    Assert.assertTrue(response.getHeader("Content-Type").contains("application/json"), "Response is not JSON");

	    // Extract only the count of users, not the whole list content
	    List<Object> usersList = response.jsonPath().getList("$");
	    Assert.assertNotNull(usersList, "User list should not be null");

	    System.out.println("Total users count: " + usersList.size());
	}
	


	@Test(dataProvider = "GetUserByUserFirstNameData",  dependsOnMethods = {"testCreateUser"}, dataProviderClass = TestDataProvider.class, priority = 5)
	public void testGetUserByFirstName(Map<String, Object> testCase) throws Exception {
	    String firstName = ApiHelper.getUserFirstname();
	    Assert.assertNotNull(firstName, "User firstname should be set from createUser test");

	    // Assuming testCase has an endpoint template with placeholder
	    String endpointTemplate = (String) testCase.get("endpoint");
	    String endpoint = endpointTemplate.replace("{{validUserFirstName}}", firstName);

	    // You can call a helper method to send GET with full endpoint string
	    Response getResponse = ApiHelper.sendGetUserByFirstNameRequest(endpoint);
	    
	    System.out.println("FirstName from ApiHelper: " + firstName);

	    Assert.assertEquals(getResponse.getStatusCode(), (int) testCase.getOrDefault("expectedStatusCode", 200));
	}
}
	
//	@Test(dataProvider = "DeleteUserByUserFirstNameData", dataProviderClass = TestDataProvider.class, dependsOnMethods = {"testCreateUser1"}, priority = 8)
//	public void testDeleteUserByuserFirstName(Map<String, Object> testCase) throws Exception {
//	    
//	    String firstName = ApiHelper.getUserFirstname();
//	    Assert.assertNotNull(firstName, "User firstname should be set from createUser test");
//
//	    // Extract endpoint template from testCase
//	    String endpointTemplate = (String) testCase.get("endpoint");
//
//	    // Replace placeholder with actual firstName
//	    String endpoint = endpointTemplate.replace("{{validUserFirstName}}", firstName);
//
//	    // Call helper method with the fully resolved endpoint string
//	    Response deleteResponse = ApiHelper.sendDeleteUserByFirstNameRequest(endpoint);
//
//	    // Assert HTTP status code
//	    int expectedStatusCode = (int) testCase.getOrDefault("expectedStatusCode", 200);
//	    Assert.assertEquals(deleteResponse.getStatusCode(), expectedStatusCode, "DELETE request failed");
//
//	    // Optionally print response
//	    System.out.println("DELETE Response Body: " + deleteResponse.getBody().asPrettyString());
//
//	    // Clear stored userId and firstname after delete
//	   // ApiHelper.setUserId(null);
//	   // ApiHelper.setUserFirstname(null);
//	}
//
//	



