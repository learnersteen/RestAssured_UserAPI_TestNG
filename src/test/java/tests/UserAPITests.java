package tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Map;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import api.BaseTest01;
import handlers.PostUserHandler;
import handlers.PutUserHandler;
import utils.TestDataProvider;

public class UserAPITests extends BaseTest01 {
	
	private static ThreadLocal<Integer> createdUserId = new ThreadLocal<>();
	
	// variable to store the userID after createUser POST is executed 	
    //private Integer createdUserId; 
    
    @Test(dataProvider = "postUserData", dataProviderClass = TestDataProvider.class, priority = 1)
    public void testCreateUser(Map<String, Object> testCase) throws Exception {
    	  Response postResponse = PostUserHandler.handlePostUserTest(testCase);
    	  
    	  assertEquals(postResponse.getStatusCode(), 201, "User creation failed.");
          createdUserId.set(postResponse.jsonPath().getInt("userId"));
          assertNotNull(createdUserId.get(), "User ID not returned in response.");
          
          Reporter.log("Captured userId in test class: " + createdUserId.get(), true);
    }
        
//        createdUserId = postResponse.jsonPath().getInt("userId");
//        Assert.assertNotNull(createdUserId, "User ID is null in response");
//        System.out.println("Captured userId in test class: " + createdUserId);
//    }
    
    @Test(dataProvider = "putUserData", dataProviderClass = TestDataProvider.class, priority = 2, dependsOnMethods="testCreateUser")
    public void testUpdateUser(Map<String, Object> testCase) throws Exception {
    	
    	Integer userId = createdUserId.get();
        assertNotNull(userId, "User ID is null. Cannot proceed with update.");

        testCase.put("userId", userId);
        Response putResponse = PutUserHandler.handlePutUserTest(testCase);

        assertEquals(putResponse.getStatusCode(), 200, "User update failed.");
        Reporter.log("Updated userId: " + putResponse.jsonPath().getInt("userId"), true);
    }
}
    	
////    	if (createdUserId == null) {
////            throw new IllegalStateException("User ID is null. Cannot proceed with update.");
////        }
////    	
////    	testCase.put("userId", createdUserId);
////        Response putResponse = PutUserHandler.handlePutUserTest(testCase);
////        
////        
////        createdUserId = putResponse.jsonPath().getInt("userId");
//        System.out.println("Captured userId in test class: " + createdUserId);
//    
//    }
//}