package tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Map;

import org.testng.annotations.Test;

import api.BaseTest;
import handlers.PostUserHandler;
import io.restassured.response.Response;

import utils.ApiHelper;
import utils.TestDataProvider;

public class userAPITests04 extends BaseTest {
		
		private static ThreadLocal<Integer> createdUserId = new ThreadLocal<>();
		
		// variable to store the userID after createUser POST is executed 	
	    //private Integer createdUserId; 
	    
	    @Test(dataProvider = "postUserData", dataProviderClass = TestDataProvider.class, priority = 1)
	    public void testCreateUser(Map<String, Object> testCase) throws Exception {
	    	  Response postResponse = PostUserHandler.handlePostUserTest(testCase);
	    	  
	    	  assertEquals(postResponse.getStatusCode(), 201, "User creation failed.");
	          createdUserId.set(postResponse.jsonPath().getInt("userId"));
	          assertNotNull(createdUserId.get(), "User ID not returned in response.");
	          
	          //Reporter.log("Captured userId in test class: " + createdUserId.get(), true);
	    }
	    
}