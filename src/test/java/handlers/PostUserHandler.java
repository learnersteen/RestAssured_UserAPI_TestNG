package handlers;

import io.restassured.response.Response;
import pojo.UserDetails;
import utils.ApiHelper;

import java.util.Map;

import org.testng.Assert;

public class PostUserHandler {

    public static Response handlePostUserTest(Map<String, Object> testCase) throws Exception {
        String testCaseId = (String) testCase.get("testCaseId");
        System.out.println("Running test case: " + testCaseId);

        String endpoint = (String) testCase.get("endpoint");
        int expectedStatusCode = (int) testCase.get("expectedStatusCode");

        UserDetails user = ApiHelper.buildUserFromMap(testCase);
        Response response = ApiHelper.sendPostRequest(endpoint, user);

        //System.out.println("POST Response: " + response.getBody().asPrettyString());

        try {
            Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");

            if (expectedStatusCode == 201) {
                validateSuccessfulPostResponse(response, user);
                
                Integer userId = response.jsonPath().getInt("userId");
                ApiHelper.setUserId(userId);
                
            } else {
                validateErrorPostResponse(response, testCase);
            }

            System.out.println("Test Passed for: " + testCaseId);
        } catch (AssertionError e) {
            System.err.println("Test Failed for: " + testCaseId + " - Reason: " + e.getMessage());
            throw e;
        }
        
        return response; 
    }
    
  

    private static void validateSuccessfulPostResponse(Response response, UserDetails user) {
        Assert.assertEquals(response.jsonPath().getString("userFirstName"), user.getUserFirstName(), "Mismatch in userFirstName");
        Assert.assertEquals(response.jsonPath().getString("userLastName"), user.getUserLastName(), "Mismatch in userLastName");
        Assert.assertEquals(response.jsonPath().getLong("userContactNumber"), user.getUserContactNumber(), "Mismatch in userContactNumber");
        Assert.assertEquals(response.jsonPath().getString("userEmailId"), user.getUserEmailId(), "Mismatch in userEmailId");

        Assert.assertEquals(response.jsonPath().getString("userAddress.plotNumber"), user.getUserAddress().getPlotNumber(), "Mismatch in plotNumber");
        Assert.assertEquals(response.jsonPath().getString("userAddress.street"), user.getUserAddress().getStreet(), "Mismatch in street");
        Assert.assertEquals(response.jsonPath().getString("userAddress.state"), user.getUserAddress().getState(), "Mismatch in state");
        Assert.assertEquals(response.jsonPath().getString("userAddress.country"), user.getUserAddress().getCountry(), "Mismatch in country");
        Assert.assertEquals(response.jsonPath().getInt("userAddress.zipCode"), user.getUserAddress().getZipCode(), "Mismatch in zipCode");

        Assert.assertNotNull(response.jsonPath().getInt("userId"), "userId should be auto-generated and present");
        Assert.assertNotNull(response.jsonPath().getString("creationTime"), "creationTime should be auto-generated and present");
        Assert.assertNotNull(response.jsonPath().getString("lastModTime"), "lastModTime should be auto-generated and present");
        Assert.assertNotNull(response.jsonPath().getInt("userAddress.addressId"), "addressId should be auto-generated and present");
    }

    private static void validateErrorPostResponse(Response response, Map<String, Object> testCase) {
        String actualErrorMessage = response.jsonPath().getString("message");
        if (actualErrorMessage == null) {
            actualErrorMessage = response.jsonPath().getString("errorMessage");
        }

        String actualStatusLine = response.getStatusLine();
        String expectedErrorMessage = (String) testCase.get("expectedErrorMessage");
        String expectedStatusText = (String) testCase.get("expectedStatusText");

        Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message mismatch");
        Assert.assertTrue(actualStatusLine.contains(expectedStatusText), "Status line mismatch");

        System.out.println("Actual Status Line  : " + actualStatusLine);
        System.out.println("Expected Status Text: " + expectedStatusText);
    }
}