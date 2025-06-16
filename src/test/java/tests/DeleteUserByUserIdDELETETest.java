package tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import apirequesthandlers.CreateUserPOSTRequest;
import apirequesthandlers.DeleteUserByIdDELETERequest;
import hooks.Hooks;
import io.restassured.response.Response;
import pojo.UserDetails;
import utils.APIHelperClass;
import utils.TestDataProvider;

public class DeleteUserByUserIdDELETETest extends Hooks {
	
	
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
	
	@Test(dataProvider = "DeleteByUserIdTestData", dataProviderClass = TestDataProvider.class)
	public void testDeleteByUserId(Map<String, Object> testCase) throws Exception {
	    // Get userId and firstName saved during @BeforeMethod user creation
	    Integer userId = APIHelperClass.getUserId();
	    String firstName = APIHelperClass.getUserFirstname();

	    Assert.assertNotNull(userId, "UserId should be set from createUser test");
	    Assert.assertNotNull(firstName, "UserFirstName should be set from createUser test");

	    System.out.println("Testing DELETE user with UserId: " + userId);

	    // Replace placeholder in endpoint with actual userId if needed
	    String endpoint = (String) testCase.get("endpoint");
	    endpoint = endpoint.replace("{{validUserId}}", String.valueOf(userId));  // if your endpoint supports userId

	    // Send DELETE request (assuming your DeleteUserByIdDELETERequest can accept the endpoint dynamically)
	    Response deleteResponse = DeleteUserByIdDELETERequest.sendDeleteByUserIdRequest(testCase);

	    // Validate status code
	    int expectedStatus = (int) testCase.getOrDefault("expectedStatusCode", 200);
	    Assert.assertEquals(deleteResponse.getStatusCode(), expectedStatus, "Status code mismatch");

	    // Minimal JSON structure check
	    String responseBody = deleteResponse.getBody().asString();
	    Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, Map.class);

	    Assert.assertTrue(responseMap.containsKey("status"), "Response JSON missing 'status'");
	    Assert.assertTrue(responseMap.get("status") instanceof String, "'status' should be a String");

	    Assert.assertTrue(responseMap.containsKey("message"), "Response JSON missing 'message'");
	    Assert.assertTrue(responseMap.get("message") instanceof String, "'message' should be a String");

	    // Optional: Check exact message text from your JSON test data
	    if(testCase.containsKey("expectedStatusMessage")){
	        Assert.assertEquals(responseMap.get("message"), testCase.get("expectedStatusMessage"));
	    }
	}
	
	 @AfterMethod
	    public void clearSavedData() {
	        // Clear saved data after each test method to keep tests isolated and clean
		 APIHelperClass.setUserId(null);
		 APIHelperClass.setUserFirstname(null);
		 APIHelperClass.setOriginalUser(null);

		 // To check if the values are now null, retrieve them and print:
		 Integer userId = APIHelperClass.getUserId();              // assuming getUserId returns Integer
		 String userFirstName = APIHelperClass.getUserFirstname(); // assuming it returns String
		 Object originalUser = APIHelperClass.getOriginalUser();   // assuming it returns an Object or UserDetails

		 System.out.println("UserId after set to null: " + userId);
		 System.out.println("UserFirstName after set to null: " + userFirstName);
		 System.out.println("OriginalUser after set to null: " + originalUser);
	    }
	}

	




