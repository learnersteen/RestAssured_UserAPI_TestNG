package tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import apirequesthandlers.CreateUserPOSTRequest;
import apirequesthandlers.DeleteUserByFirstNameDELETERequest;
import apirequesthandlers.GetUserByUserFirstNameGETRequest;
import hooks.Hooks;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import pojo.UserAddress;
import pojo.UserDetails;
import utils.APIHelperClass;
import utils.TestDataProvider;

public class GetUserByUserFirstNameGETTest extends Hooks {
	
	private ObjectMapper mapper = new ObjectMapper();

	@BeforeMethod
	public void createUserBeforeGetByFirstNameTest() throws Exception {
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
	
	@Test(dataProvider = "GetUserByFirstNameTestData", dataProviderClass = TestDataProvider.class)
	public void testGetByUserId(Map<String, Object> testCase) throws Exception {
	    // Ensure firstname and userId are set from POST test
	    String firstName = APIHelperClass.getUserFirstname();
	    Integer userId = APIHelperClass.getUserId();

	    Assert.assertNotNull(userId, "UserId should be set from createUser test");
	    Assert.assertNotNull(firstName, "UserFirstName should be set from createUser test");

	    System.out.println("Testing GET user with UserFirstName: " + firstName);
	    System.out.println("Testing GET user with UserId: " + userId);

	    // Call GET method
	    Response getResponse = GetUserByUserFirstNameGETRequest.sendGetByUserFirstNameRequest(testCase);

	    // Status code validations
	    int expectedStatus = (int) testCase.getOrDefault("expectedStatusCode", 200);
	    Assert.assertEquals(getResponse.getStatusCode(), expectedStatus, "Status code mismatch in GET by userId request");

	    if (testCase.containsKey("jsonSchema")) {
	        String schemaPath = (String) testCase.get("jsonSchema");
	        getResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
	    }

	    String responseBody = getResponse.getBody().asString();

	    if (responseBody.trim().startsWith("[")) {
	        // Multiple users in response (JSON Array)
	        List<Map<String, Object>> users = getResponse.jsonPath().getList("$");

	        Assert.assertFalse(users.isEmpty(), "Response returned empty list for given first name");

	        for (Map<String, Object> userMap : users) {
	            // Validate each user fields types
	            Object userIdObj = userMap.get("userId");
	            Assert.assertTrue(userIdObj instanceof Number, "Each userId should be a Number but was: " + userIdObj.getClass().getSimpleName());

	            Assert.assertTrue(userMap.get("userFirstName") instanceof String, "userFirstName should be String");
	            Assert.assertTrue(userMap.get("userLastName") instanceof String, "userLastName should be String");

	            Object contactNumber = userMap.get("userContactNumber");
	            Assert.assertTrue(contactNumber instanceof Number, "userContactNumber should be a Number but was: " + contactNumber.getClass().getSimpleName());

	            Assert.assertTrue(userMap.get("userEmailId") instanceof String, "userEmailId should be String");
	            Assert.assertTrue(userMap.get("creationTime") instanceof String, "creationTime should be ISO date string");
	            Assert.assertTrue(userMap.get("lastModTime") instanceof String, "lastModTime should be ISO date string");

	            Map<String, Object> userAddress = (Map<String, Object>) userMap.get("userAddress");
	            Assert.assertTrue(userAddress.get("addressId") instanceof Number, "addressId should be Number");
	            Assert.assertTrue(userAddress.get("plotNumber") instanceof String, "plotNumber should be String");
	            Assert.assertTrue(userAddress.get("street") instanceof String, "street should be String");
	            Assert.assertTrue(userAddress.get("state") instanceof String, "state should be String");
	            Assert.assertTrue(userAddress.get("country") instanceof String, "country should be String");
	            Assert.assertTrue(userAddress.get("zipCode") instanceof Number, "zipCode should be Number");

	            Assert.assertEquals(userMap.get("userFirstName"), firstName, "Mismatch in userFirstName for returned user");
	            System.out.println("User matched: " + userIdObj);
	        }
	    } else {
	        // Single user object
	        UserDetails actualUser = mapper.readValue(responseBody, UserDetails.class);
	        UserDetails expectedUser = APIHelperClass.getOriginalUser();

	        Assert.assertEquals(actualUser.getUserFirstName(), expectedUser.getUserFirstName());
	        Assert.assertEquals(actualUser.getUserLastName(), expectedUser.getUserLastName());
	        Assert.assertEquals(actualUser.getUserContactNumber(), expectedUser.getUserContactNumber());
	        Assert.assertEquals(actualUser.getUserEmailId(), expectedUser.getUserEmailId());

	        if (expectedUser.getUserId() != null) {
	            Assert.assertEquals(actualUser.getUserId(), expectedUser.getUserId(), "Mismatch in userId");
	        }

	        if (expectedUser.getCreationTime() != null) {
	            Assert.assertEquals(actualUser.getCreationTime(), expectedUser.getCreationTime(), "Mismatch in creationTime");
	        }

	        if (expectedUser.getLastModTime() != null) {
	            Assert.assertEquals(actualUser.getLastModTime(), expectedUser.getLastModTime(), "Mismatch in lastModTime");
	        }

	        // Address comparison
	        UserAddress expectedAddress = expectedUser.getUserAddress();
	        UserAddress actualAddress = actualUser.getUserAddress();

	        if (expectedAddress.getAddressId() != null) {
	            Assert.assertEquals(actualAddress.getAddressId(), expectedAddress.getAddressId(), "Mismatch in addressId");
	        }

	        Assert.assertEquals(actualAddress.getPlotNumber(), expectedAddress.getPlotNumber());
	        Assert.assertEquals(actualAddress.getStreet(), expectedAddress.getStreet());
	        Assert.assertEquals(actualAddress.getState(), expectedAddress.getState());
	        Assert.assertEquals(actualAddress.getCountry(), expectedAddress.getCountry());
	        Assert.assertEquals(actualAddress.getZipCode(), expectedAddress.getZipCode());

	        APIHelperClass.setUserId(actualUser.getUserId());
	        APIHelperClass.setUserFirstname(actualUser.getUserFirstName());
	    }
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
	        Response deleteResponse = DeleteUserByFirstNameDELETERequest.sendDeleteUserByFirstNameRequest(deleteTestCase);
	        Assert.assertEquals(deleteResponse.getStatusCode(), 200, "Failed to delete user");

	        // Clear saved data
	        APIHelperClass.setUserId(null);
	        APIHelperClass.setUserFirstname(null);
	        APIHelperClass.setOriginalUser(null);
	    }
	}
}