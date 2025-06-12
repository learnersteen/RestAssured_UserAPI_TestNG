package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.UserAddress;
import pojo.UserDetails;
import api.BaseTest;

import java.util.Map;

public class ApiHelper {

    public static UserDetails buildUserFromMap(Map<String, Object> testCase) {
        UserDetails user = new UserDetails();
        UserAddress address = new UserAddress();

        if (testCase.containsKey("userFirstName")) user.setUserFirstName((String) testCase.get("userFirstName"));
        if (testCase.containsKey("userLastName")) user.setUserLastName((String) testCase.get("userLastName"));
        if (testCase.containsKey("userContactNumber")) user.setUserContactNumber(Long.parseLong(testCase.get("userContactNumber").toString()));
        if (testCase.containsKey("userEmailId")) user.setUserEmailId((String) testCase.get("userEmailId"));

        if (testCase.containsKey("plotNumber")) address.setPlotNumber((String) testCase.get("plotNumber"));
        if (testCase.containsKey("street")) address.setStreet((String) testCase.get("street"));
        if (testCase.containsKey("state")) address.setState((String) testCase.get("state"));
        if (testCase.containsKey("country")) address.setCountry((String) testCase.get("country"));
        if (testCase.containsKey("zipCode")) address.setZipCode(Integer.parseInt(testCase.get("zipCode").toString()));

        address.setAddressId(null);
        user.setUserAddress(address);
        user.setUserId(null);

        return user;
    }

    public static Response sendPostRequest(String endpoint, UserDetails user) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        RequestSpecification spec = BaseTest.getRequestWithBasicAuth();
        
        System.out.println("Sending POST request to endpoint: " + endpoint);
        System.out.println("POST Request Body: " + requestBody);
        
        //return spec.body(requestBody).post(endpoint);
        Response response = spec.body(requestBody).post(endpoint);
        
        System.out.println("POST Response Status Code: " + response.getStatusCode());
        System.out.println("POST Response Body: " + response.getBody().asPrettyString());
        
		return response;
		
		
    }
    
    public static Response sendPutRequest(String endpoint, UserDetails user) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        RequestSpecification spec = BaseTest.getRequestWithBasicAuth();

        System.out.println("Sending PUT request to endpoint: " + endpoint);
        System.out.println("PUT Request Body: " + requestBody);

        Response response = spec.body(requestBody).put(endpoint);

        System.out.println("PUT Response Status Code: " + response.getStatusCode());
        System.out.println("PUT Response Body: " + response.getBody().asPrettyString());

        return response;
    }
 
    
    private static Integer storedUserId;

    public static void setUserId(Integer userId) {
        storedUserId = userId;
    }

    public static Integer getUserId() {
        return storedUserId;
    }
    
    
    
    
     
}