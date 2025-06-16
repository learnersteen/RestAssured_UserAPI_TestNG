//package tests;
//
//import java.util.List;
//import java.util.Map;
//
//import org.testng.Assert;
//import org.testng.annotations.Test;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
//import api.BaseTest;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import pojo.UserAddress;
//import pojo.UserDetails;
//import utils.TestCaseLoader;
//	
//	public class test03  extends BaseTest {
//
//	
//	    @Test(priority = 1)
//	    public void userAPITests() throws Exception {
//
//	        List<Map<String, Object>> testCases = TestCaseLoader.loadTestCases("post_put_patch_delete_get.json");
//	        ObjectMapper mapper = new ObjectMapper(); // For converting POJO to JSON
//	        
//	        // Create map for HTTP methods (for now only POST)
//	       // Map<String, Function<String, Response>> httpMethods = new HashMap<>();
//	        
//	      
//	        for (Map<String, Object> testCase : testCases) {
//	            String method = (String) testCase.get("method");
//	            String endpoint = (String) testCase.get("endpoint");
//	            int expectedStatusCode = (int) testCase.get("expectedStatusCode");
//	            
//	            if (!method.equalsIgnoreCase("POST")) continue; // Handle only POST for now
//
//	            // Set UserDetails
//	            UserDetails user = new UserDetails();
//	            
//	            if (testCase.containsKey("userFirstName")) user.setUserFirstName((String) testCase.get("userFirstName"));
//	            if (testCase.containsKey("userLastName")) user.setUserLastName((String) testCase.get("userLastName"));
//	            if (testCase.containsKey("userContactNumber")) user.setUserContactNumber(Long.parseLong(testCase.get("userContactNumber").toString()));
//	            if (testCase.containsKey("userEmailId")) user.setUserEmailId((String) testCase.get("userEmailId"));
//
//	            // Set Address
//	            UserAddress userAddress = new UserAddress();
//	            if (testCase.containsKey("plotNumber")) userAddress.setPlotNumber((String) testCase.get("plotNumber"));
//	            if (testCase.containsKey("street")) userAddress.setStreet((String) testCase.get("street"));
//	            if (testCase.containsKey("state")) userAddress.setState((String) testCase.get("state"));
//	            if (testCase.containsKey("country")) userAddress.setCountry((String) testCase.get("country"));
//	            //if (testCase.containsKey("zipCode")) userAddress.setZipCode((int) testCase.get("zipCode"));
//	            
//	            
//	            if (testCase.containsKey("zipCode")) { 
//	                Object zip = testCase.get("zipCode");
//	                userAddress.setZipCode(Integer.parseInt(zip.toString()));
//	            }
//
//	            
//	         // Explicitly nullify IDs so they won't be sent in the request
//	            user.setUserId(null);
//	            userAddress.setAddressId(null);
//	            user.setUserAddress(userAddress);
//
//	            // Convert to JSON String
//	            String requestBody = mapper.writeValueAsString(user);
//	            
//	            // Prepare request spec with auth and headers
//	            RequestSpecification requestSpec = BaseTest.getRequestWithBasicAuth();
//	            
//	            Response response = requestSpec
//	                    .body(requestBody)
//	                    .post(endpoint);
//	            
//	            System.out.println("Response: " + response.getBody().asPrettyString());
//	            
//	         // === Validate status code ===
//	            Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");
//	            
//	            if (expectedStatusCode == 201) {
//	                // Validate auto-generated fields
//	                String userId = response.jsonPath().getString("userId");
//	                String addressId = response.jsonPath().getString("userAddress.addressId");
//	                String creationTime = response.jsonPath().getString("creationTime");
//	                String modificationTime = response.jsonPath().getString("lastModTime");
//
//	                Assert.assertNotNull(userId, "userId should not be null");
//	                Assert.assertNotNull(addressId, "addressId should not be null");
//	                Assert.assertNotNull(creationTime, "creationTime should not be null");
//	                Assert.assertNotNull(modificationTime, "modificationTime should not be null");
//
//	                // Validate response fields match input
//	                Assert.assertEquals(response.jsonPath().getString("userFirstName"), user.getUserFirstName(), "userFirstName mismatch");
//	                Assert.assertEquals(response.jsonPath().getString("userLastName"), user.getUserLastName(), "userLastName mismatch");
//	                Assert.assertEquals(response.jsonPath().getString("userContactNumber"), String.valueOf(user.getUserContactNumber()), "userContactNumber mismatch");
//	                Assert.assertEquals(response.jsonPath().getString("userEmailId"), user.getUserEmailId(), "userEmailId mismatch");
//
//	                Assert.assertEquals(response.jsonPath().getString("userAddress.plotNumber"), userAddress.getPlotNumber(), "plotNumber mismatch");
//	                Assert.assertEquals(response.jsonPath().getString("userAddress.street"), userAddress.getStreet(), "street mismatch");
//	                Assert.assertEquals(response.jsonPath().getString("userAddress.state"), userAddress.getState(), "state mismatch");
//	                Assert.assertEquals(response.jsonPath().getString("userAddress.country"), userAddress.getCountry(), "country mismatch");
//	                Assert.assertEquals((int) response.jsonPath().getInt("userAddress.zipCode"), userAddress.getZipCode(), "zipCode mismatch");
//
//	            } else {
//	                //Negative scenario: Check error message exists
//	                String errorMessage = response.jsonPath().getString("errorMessage");
//	                Assert.assertNotNull(errorMessage, "Expected errorMessage in error response");
//	            }
//	        }
//	    }
//	
//	}
//
//	
