package tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import apirequesthandlers.CreateUserPOSTRequest;
import apirequesthandlers.DeleteUserByFirstNameDELETERequest;
import hooks.Hooks;
import io.restassured.response.Response;
import pojo.UserDetails;
import utils.APIHelperClass;
import utils.TestDataProvider;

public class DeleteUserByUserNameDELETETest extends Hooks {

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeMethod
    public void createUserBeforeDeleteByNameTest() throws Exception {
        // 1. Get POST user creation test data (first record)
    	 Object[][] createUserTestData = TestDataProvider.createUserTestData();
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

    @Test(dataProvider = "DeleteUserByUserFirstNameData", dataProviderClass = TestDataProvider.class, priority = 6)
    public void testDeleteUserByFirstName(Map<String, Object> testCase) throws Exception {
        // Get the saved firstName
        String firstName = APIHelperClass.getUserFirstname();
        Assert.assertNotNull(firstName, "User firstname should be set from createUser test");

        // Replace placeholder in endpoint with actual firstname
        String endpoint = (String) testCase.get("endpoint");
        endpoint = endpoint.replace("{{validUserFirstName}}", firstName);

        // Create a new map copying testCase and override endpoint with actual value
        Map<String, Object> actualTestCase = new HashMap<>(testCase);
        actualTestCase.put("endpoint", endpoint);

        System.out.println("Deleting user with firstname: " + firstName);

        // Call DELETE method
        Response deleteResponse = DeleteUserByFirstNameDELETERequest.sendDeleteUserByFirstNameRequest(actualTestCase);

        // 1. Status Code Validation
        int expectedStatus = (int) actualTestCase.getOrDefault("expectedStatusCode", 200);
        Assert.assertEquals(deleteResponse.getStatusCode(), expectedStatus, "Status code mismatch in DELETE request");

        // 2. Response Body Validation (Optional)
        if (actualTestCase.containsKey("expectedStatusMessage")) {
            String expectedMessage = (String) actualTestCase.get("expectedStatusMessage");
            String actualMessage = deleteResponse.jsonPath().getString("message");
            Assert.assertEquals(actualMessage, expectedMessage, "Response message mismatch");
        }

        // 3. Log final response
        System.out.println("Delete Response: " + deleteResponse.getBody().asString());
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