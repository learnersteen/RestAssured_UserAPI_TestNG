package apirequesthandlers;

import java.util.Map;
import hooks.Hooks;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.UserDetails;
import utils.APIHelperClass;

public class UpdateUserPUTRequest {
	

	 public static Response sendPutRequest(Map<String, Object> testCase) throws Exception {
	        UserDetails user = APIHelperClass.buildUserFromMap(testCase);
	        
	        String endpoint = (String) testCase.get("endpoint");
	     
	        Integer userId = APIHelperClass.getUserId();
	        if (userId == null) {
	            throw new IllegalStateException("UserId is not set. Ensure CreateUser runs before UpdateUser.");
	        }
	  
	        String finalEndpoint = endpoint.replace("{{validUserId}}", String.valueOf(userId));
	        String requestBody = APIHelperClass.convertUserToJson(user);

	        RequestSpecification spec = Hooks.validAuthAndHeader();

	        System.out.println("Sending PUT to: " + finalEndpoint);
	        System.out.println("PUT Body: " + requestBody);

	        return spec.body(requestBody).put(finalEndpoint);
	    }
	}
