package handlers;

import io.restassured.response.Response;
import pojo.UserDetails;
import utils.ApiHelper;

import java.util.Map;

import org.testng.Assert;

public class PutUserHandler {

    public static Response handlePutUserTest(Map<String, Object> testCase) throws Exception {
        String testCaseId = (String) testCase.get("testCaseId");
        System.out.println("Running PUT test case: " + testCaseId);

        String endpoint = (String) testCase.get("endpoint");
        int expectedStatusCode = (int) testCase.get("expectedStatusCode");

        // Replace {{userId}} in endpoint with stored userId
        if (endpoint.contains("{{userId}}")) {
            Integer userId = ApiHelper.getUserId();
            if (userId == null) {
                throw new Exception("UserId is not set from previous response. Cannot proceed with PUT.");
            }
            endpoint = endpoint.replace("{{userId}}", String.valueOf(userId));
        }

        UserDetails user = ApiHelper.buildUserFromMap(testCase);
        Response response = ApiHelper.sendPutRequest(endpoint, user);

        System.out.println("PUT Response: " + response.getBody().asPrettyString());

        try {
            Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");

            if (expectedStatusCode == 200) {
                validateSuccessfulPutResponse(response, user);

                // Extract and store userId from PUT response (if returned)
                Integer userId = response.jsonPath().getInt("userId");
                if (userId != null) {
                    ApiHelper.setUserId(userId);
                }
            } else {
                validateErrorPutResponse(response, testCase);
            }

            System.out.println("PUT Test Passed for: " + testCaseId);
        } catch (AssertionError e) {
            System.err.println("PUT Test Failed for: " + testCaseId + " - Reason: " + e.getMessage());
            throw e;
        }

        return response;
    }

    private static void validateSuccessfulPutResponse(Response response, UserDetails user) {
        Assert.assertEquals(response.jsonPath().getString("userFirstName"), user.getUserFirstName(), "Mismatch in userFirstName");
        Assert.assertEquals(response.jsonPath().getString("userLastName"), user.getUserLastName(), "Mismatch in userLastName");
        Assert.assertEquals(response.jsonPath().getLong("userContactNumber"), user.getUserContactNumber(), "Mismatch in userContactNumber");
        Assert.assertEquals(response.jsonPath().getString("userEmailId"), user.getUserEmailId(), "Mismatch in userEmailId");

        Assert.assertEquals(response.jsonPath().getString("userAddress.plotNumber"), user.getUserAddress().getPlotNumber(), "Mismatch in plotNumber");
        Assert.assertEquals(response.jsonPath().getString("userAddress.street"), user.getUserAddress().getStreet(), "Mismatch in street");
        Assert.assertEquals(response.jsonPath().getString("userAddress.state"), user.getUserAddress().getState(), "Mismatch in state");
        Assert.assertEquals(response.jsonPath().getString("userAddress.country"), user.getUserAddress().getCountry(), "Mismatch in country");
        Assert.assertEquals(response.jsonPath().getInt("userAddress.zipCode"), user.getUserAddress().getZipCode(), "Mismatch in zipCode");

        Assert.assertNotNull(response.jsonPath().getInt("userId"), "userId should be present");
    }

    private static void validateErrorPutResponse(Response response, Map<String, Object> testCase) {
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
