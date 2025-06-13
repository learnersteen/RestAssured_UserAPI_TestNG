package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.UserAddress;
import pojo.UserDetails;
import api.BaseTest;

import java.util.Map;

public class ApiHelper {

    private static Integer userId;
    private static String userFirstname;

    public static void setUserId(Integer id) {
        userId = id;
    }

    public static Integer getUserId() {
        return userId;
    }

    public static void setUserFirstname(String firstname) {
        userFirstname = firstname;
    }

    public static String getUserFirstname() {
        return userFirstname;
    }

    public static UserDetails buildUserFromMap(Map<String, Object> testCase) {
        UserDetails user = new UserDetails();

        if (testCase.containsKey("userFirstName"))
            user.setUserFirstName((String) testCase.get("userFirstName"));

        if (testCase.containsKey("userLastName"))
            user.setUserLastName((String) testCase.get("userLastName"));

        if (testCase.containsKey("userContactNumber"))
            user.setUserContactNumber(Long.parseLong(testCase.get("userContactNumber").toString()));

        if (testCase.containsKey("userEmailId"))
            user.setUserEmailId((String) testCase.get("userEmailId"));

        // Check if any address field exists in testCase
        boolean hasAddressData = testCase.containsKey("plotNumber") ||
                                 testCase.containsKey("street") ||
                                 testCase.containsKey("state") ||
                                 testCase.containsKey("country") ||
                                 testCase.containsKey("zipCode");

        if (hasAddressData) {
            UserAddress address = new UserAddress();

            if (testCase.containsKey("plotNumber"))
                address.setPlotNumber((String) testCase.get("plotNumber"));

            if (testCase.containsKey("street"))
                address.setStreet((String) testCase.get("street"));

            if (testCase.containsKey("state"))
                address.setState((String) testCase.get("state"));

            if (testCase.containsKey("country"))
                address.setCountry((String) testCase.get("country"));

            if (testCase.containsKey("zipCode"))
                address.setZipCode(Integer.parseInt(testCase.get("zipCode").toString()));

            address.setAddressId(null);  // as before
            user.setUserAddress(address);
        } else {
            user.setUserAddress(null);  // Important: set to null if no address data
        }

        user.setUserId(null);

        return user;
    }


    //METHOD ==> CREATE USER -> POST
    public static Response sendPostRequest(Map<String, Object> testCase) throws Exception {
        UserDetails user = buildUserFromMap(testCase);
        String endpoint = (String) testCase.get("endpoint");

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        RequestSpecification spec = BaseTest.getRequestWithBasicAuth();

        System.out.println("Sending POST request to endpoint: " + endpoint);
        System.out.println("POST Request Body: " + requestBody);

        Response response = spec.body(requestBody).post(endpoint);

        System.out.println("POST Response Status Code: " + response.getStatusCode());
        System.out.println("POST Response Body: " + response.getBody().asPrettyString());

        // Save userId and userFirstname globally for next requests
        Integer id = response.jsonPath().getInt("userId");
        String firstname = response.jsonPath().getString("userFirstName");

        setUserId(id);
        setUserFirstname(firstname);

        return response;
    }

    //METHOD ==> UPDATE USER -> PUT 
    
    public static Response sendPutRequest(Map<String, Object> testCase) throws Exception {
        UserDetails user = buildUserFromMap(testCase);

        // Use the stored userId for PUT
        Integer id = getUserId();
        if (id == null) {
            throw new IllegalStateException("UserId is not set. Run POST request first.");
        }
        user.setUserId(id);

        // Construct endpoint - assuming you pass endpoint template like "/users/{userId}"
        String endpointTemplate = (String) testCase.get("endpoint");
        // Replace placeholder with actual userId
        String endpoint = endpointTemplate.replace("{{validUserId}}", id.toString());

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        RequestSpecification spec = BaseTest.getRequestWithBasicAuth();

        System.out.println("Sending PUT request to endpoint: " + endpoint);
        System.out.println("PUT Request Body: " + requestBody);

        Response response = spec.body(requestBody).put(endpoint);

        System.out.println("PUT Response Status Code: " + response.getStatusCode());
        System.out.println("PUT Response Body: " + response.getBody().asPrettyString());

        // Optionally update stored userId and userFirstname if returned in PUT response
        Integer updatedId = response.jsonPath().getInt("userId");
        if (updatedId != null) {
            setUserId(updatedId);
        }

        String updatedFirstname = response.jsonPath().getString("userFirstName");
        if (updatedFirstname != null) {
            setUserFirstname(updatedFirstname);
        }

        return response;
    }
    
    //METHOD ==>PARTIAL USER UPDATE -> PATCH 
    
    public static Response sendPatchRequest(Map<String, Object> testCase) throws Exception {
        UserDetails user = buildUserFromMap(testCase);

        // Use the stored userId for PUT
        Integer id = getUserId();
        if (id == null) {
            throw new IllegalStateException("UserId is not set. Run POST request first.");
        }
        user.setUserId(id);

        // Construct endpoint - assuming you pass endpoint template like "/users/{userId}"
        String endpointTemplate = (String) testCase.get("endpoint");
        // Replace placeholder with actual userId
        String endpoint = endpointTemplate.replace("{{validUserId}}", id.toString());

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        RequestSpecification spec = BaseTest.getRequestWithBasicAuth();

        System.out.println("Sending PATCH request to endpoint: " + endpoint);
        System.out.println("PATCH Request Body: " + requestBody);

        Response response = spec.body(requestBody).patch(endpoint);

        System.out.println("PATCH Response Status Code: " + response.getStatusCode());
        System.out.println("PATCH Response Body: " + response.getBody().asPrettyString());

        // Optionally update stored userId and userFirstname if returned in PUT response
        Integer updatedId = response.jsonPath().getInt("userId");
        if (updatedId != null) {
            setUserId(updatedId);
        }

        String updatedFirstname = response.jsonPath().getString("userFirstName");
        if (updatedFirstname != null) {
            setUserFirstname(updatedFirstname);
        }

        return response;
    }
    //METHOD ==> GET USER by  USER ID -> GET 
    public static Response sendGetUserByIdRequest(Map<String, Object> testCase) throws Exception {
        // Retrieve stored userId
        Integer id = getUserId();
        if (id == null) {
            throw new IllegalStateException("UserId is not set. Run POST request first.");
        }

        // Replace placeholder {{validUserId}} in endpoint with actual userId
        String endpointTemplate = (String) testCase.get("endpoint");
        String endpoint = endpointTemplate.replace("{{validUserId}}", id.toString());

        RequestSpecification spec = BaseTest.getRequestWithBasicAuth();

        System.out.println("Sending GET request to endpoint: " + endpoint);

        Response response = spec.get(endpoint);

        System.out.println("GET Response Status Code: " + response.getStatusCode());
        System.out.println("GET Response Body: " + response.getBody().asPrettyString());

        // Optionally update stored firstname from GET response if needed
        String fetchedFirstname = response.jsonPath().getString("userFirstName");
        if (fetchedFirstname != null) {
            setUserFirstname(fetchedFirstname);
        }

        return response;
    }
    
 //METHOD ==> DELETE  USER by  USER ID -> DELETE 
    
    public static Response sendDeleteUserByIdRequest(Map<String, Object> testCase) throws Exception {
        // Retrieve stored userId
        Integer id = getUserId();
        if (id == null) {
            throw new IllegalStateException("UserId is not set. Run POST request first.");
        }

        // Replace placeholder {{validUserId}} in endpoint with actual userId
        String endpointTemplate = (String) testCase.get("endpoint");
        String endpoint = endpointTemplate.replace("{{validUserId}}", id.toString());

        RequestSpecification spec = BaseTest.getRequestWithBasicAuth();

        System.out.println("Sending DELETE request to endpoint: " + endpoint);

        Response response = spec.delete(endpoint);

        System.out.println("DELETE Response Status Code: " + response.getStatusCode());
        System.out.println("DELETE Response Body: " + response.getBody().asPrettyString());

        // Optionally, clear stored userId and firstname after delete if you want:
        setUserId(null);
        setUserFirstname(null);

        return response;
    }
//METHOD ==> GET ALL USERS 
    
	public static Response sendGetAllUsersRequest(String endpoint) throws Exception {
	    RequestSpecification spec = BaseTest.getRequestWithBasicAuth();

	    System.out.println("Sending GET request to endpoint: " + endpoint);

	    Response response = spec.get(endpoint);

	    System.out.println("GET All Users Response Status Code: " + response.getStatusCode());
	    System.out.println("GET All Users Response Body: " + response.getBody().asPrettyString());

	    return response;
	}
    
}