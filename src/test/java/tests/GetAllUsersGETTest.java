package tests;

import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import apirequesthandlers.GetAllUsersGETRequest;
import hooks.Hooks;
import io.restassured.response.Response;
import utils.TestDataProvider;

public class GetAllUsersGETTest extends Hooks {
	
		
		private ObjectMapper mapper = new ObjectMapper();
		
		@Test(dataProvider = "GetAllUsersTestData", dataProviderClass = TestDataProvider.class)
		public void testGetAllUsers(Map<String, Object> testCase) throws Exception {
		    Response getResponse = GetAllUsersGETRequest.sendGetAllUsersRequest(testCase);

		    int expectedStatus = (int) testCase.getOrDefault("expectedStatusCode", 200);
		    Assert.assertEquals(getResponse.getStatusCode(), expectedStatus, "Status code mismatch");

		    String responseBody = getResponse.getBody().asString().trim();

		    // Check response starts with JSON array
		    Assert.assertTrue(responseBody.startsWith("["), "Response is not a JSON array");

		    // Parse list of users as Map
		    List<Map<String, Object>> users = getResponse.jsonPath().getList("$");

		    System.out.println("Total users received: " + users.size());

		    // Optional: Validate basic data types only on first user if exists
		    if (!users.isEmpty()) {
		        Map<String, Object> firstUser = users.get(0);

		        Assert.assertTrue(firstUser.get("userId") instanceof Number, "userId should be a Number");
		        Assert.assertTrue(firstUser.get("userFirstName") instanceof String, "userFirstName should be String");
		        Assert.assertTrue(firstUser.get("userEmailId") instanceof String, "userEmailId should be String");

		        Map<String, Object> userAddress = (Map<String, Object>) firstUser.get("userAddress");
		        if (userAddress != null) {
		            Assert.assertTrue(userAddress.get("zipCode") instanceof Number, "zipCode should be Number");
		            Assert.assertTrue(userAddress.get("country") instanceof String, "country should be String");
		        }
		    }
		}
}