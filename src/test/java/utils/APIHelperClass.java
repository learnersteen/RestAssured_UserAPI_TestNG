package utils;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import pojo.UserAddress;
import pojo.UserDetails;

public class APIHelperClass {
	
	//Getting and Setting Global Variables -> for UserId and UserFirstName
	
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

	  
	    //test data to POJO -> Build user details object from a map 
	    	    
	    public static UserDetails buildUserFromMap(Map<String, Object> testCase) {
	        UserDetails user = new UserDetails();

	        if (testCase.containsKey("userFirstName")) user.setUserFirstName((String) testCase.get("userFirstName"));

	        if (testCase.containsKey("userLastName")) user.setUserLastName((String) testCase.get("userLastName"));

	        if (testCase.containsKey("userContactNumber")) user.setUserContactNumber(Long.parseLong(testCase.get("userContactNumber").toString()));

	        if (testCase.containsKey("userEmailId")) user.setUserEmailId((String) testCase.get("userEmailId"));

	        // Check if any address field exists in testCase
	        boolean hasAddressData = testCase.containsKey("plotNumber") ||
	                                 testCase.containsKey("street") ||
	                                 testCase.containsKey("state") ||
	                                 testCase.containsKey("country") ||
	                                 testCase.containsKey("zipCode");

	        if (hasAddressData) {
	            UserAddress address = new UserAddress();

	            if (testCase.containsKey("plotNumber")) address.setPlotNumber((String) testCase.get("plotNumber"));

	            if (testCase.containsKey("street")) address.setStreet((String) testCase.get("street"));

	            if (testCase.containsKey("state")) address.setState((String) testCase.get("state"));

	            if (testCase.containsKey("country")) address.setCountry((String) testCase.get("country"));

	            if (testCase.containsKey("zipCode")) address.setZipCode(Integer.parseInt(testCase.get("zipCode").toString()));

	            address.setAddressId(null);  
	            user.setUserAddress(address);
	        } else {
	            user.setUserAddress(null); 
	        }

	        user.setUserId(null);

	        return user;
	    }
	    
	    //Convert user to JSON 
	    
	    public static String convertUserToJson(UserDetails user) throws Exception {
	        ObjectMapper mapper = new ObjectMapper();
	        return mapper.writeValueAsString(user);
	    }
	    
	    //validate patch response 
	    private static UserDetails originalUser;

	    public static void setOriginalUser(UserDetails user) {
	        originalUser = user;
	    }

	    public static UserDetails getOriginalUser() {
	        return originalUser;
	    }
	}
