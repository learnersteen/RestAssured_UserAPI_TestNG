package tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import apirequesthandlers.CreateUserPOSTRequest;
import apirequesthandlers.DeleteUserByUserFirstNameDELETERequest;
import apirequesthandlers.UpdateUserPUTRequest;
import hooks.Hooks;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import pojo.UserAddress;
import pojo.UserDetails;
import utils.APIHelperClass;
import utils.TestDataProvider;

public class UpdateUserPUTTest extends Hooks {
	
	 Map<String, Object> createUserData;

	    @BeforeMethod
	    public void createUserBeforePutTest() throws Exception {
	        // Call DataProvider directly
	        Object[][] createUserTestData = TestDataProvider.postUserData();

	        // Pick the first test data row
	        createUserData = (Map<String, Object>) createUserTestData[0][0];

	        // Send POST request using your existing request method
	        Response createResponse = CreateUserPOSTRequest.sendPostRequest(createUserData);

	        // Extract info from response
	        int userId = createResponse.jsonPath().getInt("userId");
	        String userFirstName = createResponse.jsonPath().getString("userFirstName");

	        // Save to helper for later use
	        APIHelperClass.setUserId(userId);
	        APIHelperClass.setUserFirstname(userFirstName);
	    }
	    
	    
	@Test(dataProvider = "UpdateUserPUTTestData", dataProviderClass = TestDataProvider.class)
	public void testCreateUser(Map<String, Object> testCase) throws Exception {

	    Response putResponse = UpdateUserPUTRequest.sendPutRequest(testCase);

	    // 1. Status Code Validation
	    int expectedStatus = (int) testCase.getOrDefault("expectedStatusCode", 200);
	    Assert.assertEquals(putResponse.getStatusCode(), expectedStatus, "Status Code Mismatch");

	    // 2. JSON Schema Validation
	    if (testCase.containsKey("jsonSchema")) {
	        String schemaPath = (String) testCase.get("jsonSchema");
	        putResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
	    }

	    // 3. Data Type Validations
	    Assert.assertTrue(putResponse.jsonPath().get("userId") instanceof Integer, "userId should be Integer");
	    Assert.assertTrue(putResponse.jsonPath().get("userFirstName") instanceof String, "userFirstName should be String");
	    Assert.assertTrue(putResponse.jsonPath().get("userLastName") instanceof String, "userLastName should be String");
	    //Assert.assertTrue(postResponse.jsonPath().get("userContactNumber") instanceof Integer, "userContactNumber should be Integer");
        
	    Object contactNumber = putResponse.jsonPath().get("userContactNumber");
	    Assert.assertTrue(contactNumber instanceof Number, "userContactNumber should be a Number but was: " + contactNumber.getClass().getSimpleName());
        
	    
	    Assert.assertTrue(putResponse.jsonPath().get("userEmailId") instanceof String, "userEmailId should be String");
        Assert.assertTrue(putResponse.jsonPath().get("creationTime") instanceof String, "creationTime should be ISO date string");
        Assert.assertTrue(putResponse.jsonPath().get("lastModTime") instanceof String, "lastModTime should be ISO date string");
       
        Map<String, Object> userAddress = putResponse.jsonPath().getMap("userAddress");

        Assert.assertTrue(userAddress.get("addressId") instanceof Integer, "addressId should be Integer");
        Assert.assertTrue(userAddress.get("plotNumber") instanceof String, "plotNumber should be String");
        Assert.assertTrue(userAddress.get("street") instanceof String, "street should be String");
        Assert.assertTrue(userAddress.get("state") instanceof String, "state should be String");
        Assert.assertTrue(userAddress.get("country") instanceof String, "country should be String");
        Assert.assertTrue(userAddress.get("zipCode") instanceof Integer, "zipCode should be Integer");
        
       
	    // 4. Deserialize actual and expected user
	    ObjectMapper mapper = new ObjectMapper();
	    UserDetails actualUser = mapper.readValue(putResponse.getBody().asString(), UserDetails.class);
	    UserDetails expectedUser = APIHelperClass.buildUserFromMap(testCase);

	    // 5. POJO Comparisons (based on fields you want)
	    Assert.assertEquals(actualUser.getUserFirstName(), expectedUser.getUserFirstName());
	    Assert.assertEquals(actualUser.getUserLastName(), expectedUser.getUserLastName());
	    Assert.assertEquals(actualUser.getUserContactNumber(), expectedUser.getUserContactNumber(), "Mismatch in userContactNumber");
	    Assert.assertEquals(actualUser.getUserEmailId(), expectedUser.getUserEmailId(), "Mismatch in userEmailId");

	    
	    UserAddress expectedAddress = expectedUser.getUserAddress();
	    UserAddress actualAddress = actualUser.getUserAddress();
	    
	    //System generated fields 
	    if (expectedUser.getUserId() != null) {
	        Assert.assertEquals(actualUser.getUserId(), expectedUser.getUserId(), "Mismatch in userId");
	    }
	    if (expectedAddress.getAddressId() != null) {
	        Assert.assertEquals(actualAddress.getAddressId(), expectedAddress.getAddressId(), "Mismatch in addressId");
	    }

	    // dates validations 
	    if (expectedUser.getCreationTime() != null) {
	        Assert.assertEquals(actualUser.getCreationTime(), expectedUser.getCreationTime(), "Mismatch in creationTime");
	    }
	    if (expectedUser.getLastModTime() != null) {
	        Assert.assertEquals(actualUser.getLastModTime(), expectedUser.getLastModTime(), "Mismatch in lastModTime");
	    }

	    //Set globals (for chaining tests)
	    
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

          String endpoint = "/uap/deleteuser/username/{{validUserFirstName}}";  
           String finalURL = endpoint.replace("{{validUserFirstName}}", firstName);
           
           Map<String, Object> deleteTestCase = Map.of(
                   "endpoint", finalURL,
                  "expectedStatusCode", 200
              );
          
          // Send delete request
           Response deleteResponse = DeleteUserByUserFirstNameDELETERequest.sendDeleteByUserFirstNameRequest(deleteTestCase);

           // Assertion
           Assert.assertEquals(deleteResponse.getStatusCode(), 200, "Failed to delete user in @AfterMethod");

           // Clear saved data
          APIHelperClass.setUserId(null);
          APIHelperClass.setUserFirstname(null);
      }
  }
}