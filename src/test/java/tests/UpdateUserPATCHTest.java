package tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import apirequesthandlers.CreateUserPOSTRequest;
import apirequesthandlers.DeleteUserByUserFirstNameDELETERequest;
import apirequesthandlers.UpdateUserPATCHRequest;
import hooks.Hooks;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import pojo.UserAddress;
import pojo.UserDetails;
import utils.APIHelperClass;
import utils.TestDataProvider;

public class UpdateUserPATCHTest extends Hooks {
    
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeMethod
    public void createUserBeforePatchTest() throws Exception {
        // 1. Get POST user creation test data
        Object[][] createUserTestData = TestDataProvider.postUserData();
        Map<String, Object> createUserData = (Map<String, Object>) createUserTestData[0][0];

        // 2. Send POST request to create user
        Response createResponse = CreateUserPOSTRequest.sendPostRequest(createUserData);
        Assert.assertEquals(createResponse.getStatusCode(), 201, "User creation failed in @BeforeMethod");

        // 3. Deserialize full original user POJO from POST response
        UserDetails originalUser = mapper.readValue(createResponse.getBody().asString(), UserDetails.class);

        // 4. Save userId, userFirstName globally for patch and cleanup
        APIHelperClass.setUserId(originalUser.getUserId());
        APIHelperClass.setUserFirstname(originalUser.getUserFirstName());

        // 5. Save full original user POJO globally as baseline for validation
        APIHelperClass.setOriginalUser(originalUser);
    }
    
    @Test(dataProvider = "UpdateUserPATCHTestData", dataProviderClass = TestDataProvider.class)
    public void testPatchUser(Map<String, Object> patchRequest) throws Exception {

        // You must add userId into patchRequest or endpoint inside sendPatchRequest method,
        // depending on your implementation, to target the right user

        // Send PATCH request
        Response patchResponse = UpdateUserPATCHRequest.sendPatchRequest(patchRequest);

        int expectedStatus = (int) patchRequest.getOrDefault("expectedStatusCode", 200);
        Assert.assertEquals(patchResponse.getStatusCode(), expectedStatus, "Status Code mismatch");

        // Optional JSON Schema validation if schema path provided
        if (patchRequest.containsKey("jsonSchema")) {
            String schemaPath = (String) patchRequest.get("jsonSchema");
            patchResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
        }

        // Deserialize PATCH response user
        UserDetails actualUser = mapper.readValue(patchResponse.getBody().asString(), UserDetails.class);

        // Get original baseline user before patch
        UserDetails originalUser = APIHelperClass.getOriginalUser();
        Assert.assertNotNull(originalUser, "Original user baseline must be saved before PATCH");

        // Now validate each field: if patched => updated, else unchanged from original
        if (patchRequest.containsKey("userFirstName")) {
            Assert.assertEquals(actualUser.getUserFirstName(), patchRequest.get("userFirstName"));
        } else {
            Assert.assertEquals(actualUser.getUserFirstName(), originalUser.getUserFirstName(), "userFirstName should be unchanged");
        }

        if (patchRequest.containsKey("userLastName")) {
            Assert.assertEquals(actualUser.getUserLastName(), patchRequest.get("userLastName"));
        } else {
            Assert.assertEquals(actualUser.getUserLastName(), originalUser.getUserLastName(), "userLastName should be unchanged");
        }

        if (patchRequest.containsKey("userContactNumber")) {
            Assert.assertEquals(actualUser.getUserContactNumber(), ((Number) patchRequest.get("userContactNumber")).longValue());
        } else {
            Assert.assertEquals(actualUser.getUserContactNumber(), originalUser.getUserContactNumber(), "userContactNumber should be unchanged");
        }

        if (patchRequest.containsKey("userEmailId")) {
            Assert.assertEquals(actualUser.getUserEmailId(), patchRequest.get("userEmailId"));
        } else {
            Assert.assertEquals(actualUser.getUserEmailId(), originalUser.getUserEmailId(), "userEmailId should be unchanged");
        }

        // For userAddress, validate similarly if applicable
        UserAddress actualAddress = actualUser.getUserAddress();
        UserAddress originalAddress = originalUser.getUserAddress();

        if (patchRequest.containsKey("userAddress.plotNumber")) {
            Assert.assertEquals(actualAddress.getPlotNumber(), patchRequest.get("userAddress.plotNumber"));
        } else {
            Assert.assertEquals(actualAddress.getPlotNumber(), originalAddress.getPlotNumber(), "plotNumber should be unchanged");
        }
        if (patchRequest.containsKey("userAddress.street")) {
            Assert.assertEquals(actualAddress.getStreet(), patchRequest.get("userAddress.street"));
        } else {
            Assert.assertEquals(actualAddress.getStreet(), originalAddress.getStreet(), "street should be unchanged");
        }
        if (patchRequest.containsKey("userAddress.state")) {
            Assert.assertEquals(actualAddress.getState(), patchRequest.get("userAddress.state"));
        } else {
            Assert.assertEquals(actualAddress.getState(), originalAddress.getState(), "state should be unchanged");
        }
        if (patchRequest.containsKey("userAddress.country")) {
            Assert.assertEquals(actualAddress.getCountry(), patchRequest.get("userAddress.country"));
        } else {
            Assert.assertEquals(actualAddress.getCountry(), originalAddress.getCountry(), "country should be unchanged");
        }
        if (patchRequest.containsKey("userAddress.zipCode")) {
            Assert.assertEquals(actualAddress.getZipCode(), patchRequest.get("userAddress.zipCode"));
        } else {
            Assert.assertEquals(actualAddress.getZipCode(), originalAddress.getZipCode(), "zipCode should be unchanged");
        }


        Assert.assertEquals(actualUser.getUserId(), originalUser.getUserId(), "userId should not change after PATCH");


        // Update baseline user to the latest patched user
        APIHelperClass.setOriginalUser(actualUser);

  
        APIHelperClass.setUserId(actualUser.getUserId());
        APIHelperClass.setUserFirstname(actualUser.getUserFirstName());
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

            Response deleteResponse = DeleteUserByUserFirstNameDELETERequest.sendDeleteByUserFirstNameRequest(deleteTestCase);
            Assert.assertEquals(deleteResponse.getStatusCode(), 200, "Failed to delete user in @AfterMethod");

            // Clear saved data
            APIHelperClass.setUserId(null);
            APIHelperClass.setUserFirstname(null);
            APIHelperClass.setOriginalUser(null);
        }
    }
}
