package tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import apirequesthandlers.CreateUserPOSTRequest;
import apirequesthandlers.DeleteUserByUserFirstNameDELETERequest;
import apirequesthandlers.GetUserByUserIdGETRequest;
import hooks.Hooks;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import pojo.UserAddress;
import pojo.UserDetails;
import utils.APIHelperClass;
import utils.TestDataProvider;

public class GetUserByUserIdGETTest extends Hooks {
	
	private ObjectMapper mapper = new ObjectMapper();

	@BeforeMethod
	public void createUserBeforeGetByIdTest() throws Exception {
	    // 1. Get POST user creation test data (first record)
	    Object[][] createUserTestData = TestDataProvider.postUserData();
	    Map<String, Object> createUserData = (Map<String, Object>) createUserTestData[0][0];

	    // 2. Send POST request to create user
	    Response createResponse = CreateUserPOSTRequest.sendPostRequest(createUserData);
	    Assert.assertEquals(createResponse.getStatusCode(), 201, "User creation failed in @BeforeMethod");

	    // 3. Deserialize UserDetails from POST response
	    UserDetails originalUser = mapper.readValue(createResponse.getBody().asString(), UserDetails.class);

	    // 4. Save userId and userFirstName globally for later tests and cleanup
	    APIHelperClass.setUserId(originalUser.getUserId());
	    APIHelperClass.setUserFirstname(originalUser.getUserFirstName());

	    // 5. Save original user POJO globally for validation
	    APIHelperClass.setOriginalUser(originalUser);
	}
	
	@Test(dataProvider = "GetUserByUserIdTestData", dataProviderClass = TestDataProvider.class)
	public void testGetByUserId(Map<String, Object> testCase) throws Exception {
	    // Ensure firstname and userId are set from POST test
	    String firstName = APIHelperClass.getUserFirstname();
	    Integer userId = APIHelperClass.getUserId();

	    Assert.assertNotNull(userId, "UserId should be set from createUser test");
	    Assert.assertNotNull(firstName, "UserFirstName should be set from createUser test");

	    System.out.println("Testing GET user with UserFirstName: " + firstName);
	    System.out.println("Testing GET user with UserId: " + userId);

	    // Call GET method
	    Response getResponse = GetUserByUserIdGETRequest.sendGetByUserIdRequest(testCase);

	    // Status code validations
	    int expectedStatus = (int) testCase.getOrDefault("expectedStatusCode", 200);
	    Assert.assertEquals(getResponse.getStatusCode(), expectedStatus, "Status code mismatch in GET by userId request");

	    if (testCase.containsKey("jsonSchema")) {
	        String schemaPath = (String) testCase.get("jsonSchema");
	        getResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
	    }

	    // Data Type Validations (same as before)
	    Assert.assertTrue(getResponse.jsonPath().get("userId") instanceof Integer, "userId should be Integer");
	    Assert.assertTrue(getResponse.jsonPath().get("userFirstName") instanceof String, "userFirstName should be String");
	    Assert.assertTrue(getResponse.jsonPath().get("userLastName") instanceof String, "userLastName should be String");

	    Object contactNumber = getResponse.jsonPath().get("userContactNumber");
	    Assert.assertTrue(contactNumber instanceof Number, "userContactNumber should be a Number but was: " + contactNumber.getClass().getSimpleName());

	    Assert.assertTrue(getResponse.jsonPath().get("userEmailId") instanceof String, "userEmailId should be String");
	    Assert.assertTrue(getResponse.jsonPath().get("creationTime") instanceof String, "creationTime should be ISO date string");
	    Assert.assertTrue(getResponse.jsonPath().get("lastModTime") instanceof String, "lastModTime should be ISO date string");

	    Map<String, Object> userAddress = getResponse.jsonPath().getMap("userAddress");
	    Assert.assertTrue(userAddress.get("addressId") instanceof Integer, "addressId should be Integer");
	    Assert.assertTrue(userAddress.get("plotNumber") instanceof String, "plotNumber should be String");
	    Assert.assertTrue(userAddress.get("street") instanceof String, "street should be String");
	    Assert.assertTrue(userAddress.get("state") instanceof String, "state should be String");
	    Assert.assertTrue(userAddress.get("country") instanceof String, "country should be String");
	    Assert.assertTrue(userAddress.get("zipCode") instanceof Integer, "zipCode should be Integer");

	    // Deserialize actual user from response
	    UserDetails actualUser = mapper.readValue(getResponse.getBody().asString(), UserDetails.class);

	    // Use the original user created in @BeforeMethod as expected user
	    UserDetails expectedUser = APIHelperClass.getOriginalUser();

	    // Compare fields
	    Assert.assertEquals(actualUser.getUserFirstName(), expectedUser.getUserFirstName());
	    Assert.assertEquals(actualUser.getUserLastName(), expectedUser.getUserLastName());
	    Assert.assertEquals(actualUser.getUserContactNumber(), expectedUser.getUserContactNumber(), "Mismatch in userContactNumber");
	    Assert.assertEquals(actualUser.getUserEmailId(), expectedUser.getUserEmailId(), "Mismatch in userEmailId");

	    UserAddress expectedAddress = expectedUser.getUserAddress();
	    UserAddress actualAddress = actualUser.getUserAddress();

	    if (expectedUser.getUserId() != null) {
	        Assert.assertEquals(actualUser.getUserId(), expectedUser.getUserId(), "Mismatch in userId");
	    }
	    if (expectedAddress.getAddressId() != null) {
	        Assert.assertEquals(actualAddress.getAddressId(), expectedAddress.getAddressId(), "Mismatch in addressId");
	    }

	    if (expectedUser.getCreationTime() != null) {
	        Assert.assertEquals(actualUser.getCreationTime(), expectedUser.getCreationTime(), "Mismatch in creationTime");
	    }
	    if (expectedUser.getLastModTime() != null) {
	        Assert.assertEquals(actualUser.getLastModTime(), expectedUser.getLastModTime(), "Mismatch in lastModTime");
	    }

	    // Save updated globals if needed
	    System.out.println("Actual UserId from response POJO: " + actualUser.getUserId());
	    System.out.println("Actual UserFirstname from response POJO: " + actualUser.getUserFirstName());

	    APIHelperClass.setUserId(actualUser.getUserId());
	    APIHelperClass.setUserFirstname(actualUser.getUserFirstName());

	    System.out.println("Saved UserId: " + APIHelperClass.getUserId());
	    System.out.println("Saved UserFirstname: " + APIHelperClass.getUserFirstname());
	}

	@AfterMethod
	public void cleanUpUser() throws Exception {
	    String firstName = APIHelperClass.getUserFirstname();
	    if (firstName != null) {
	        System.out.println("Running cleanup: Deleting user with firstname: " + firstName);

	        // Build delete endpoint URL dynamically
	        String endpoint = "/uap/deleteuser/username/{{validUserFirstName}}";  
	        String finalURL = endpoint.replace("{{validUserFirstName}}", firstName);
	        
	        Map<String, Object> deleteTestCase = Map.of(
	            "endpoint", finalURL,
	            "expectedStatusCode", 200
	        );

	        // Send DELETE request
	        Response deleteResponse = DeleteUserByUserFirstNameDELETERequest.sendDeleteByUserFirstNameRequest(deleteTestCase);
	        Assert.assertEquals(deleteResponse.getStatusCode(), 200, "Failed to delete user");

	        // Clear saved data
	        APIHelperClass.setUserId(null);
	        APIHelperClass.setUserFirstname(null);
	        APIHelperClass.setOriginalUser(null);
	    }
	}
}