package apirequesthandlers;

import java.util.Map;

import hooks.BaseTest;
import hooks.Hooks;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.UserDetails;
import utils.APIHelperClass;

public class UpdateUserPATCHRequest extends Hooks {
	
	
		 public static Response sendPatchRequest(Map<String, Object> testCase) throws Exception {
		        UserDetails user = APIHelperClass.buildUserFromMap(testCase);
		        
		        String endpoint = (String) testCase.get("endpoint");
		     
		        Integer userId = APIHelperClass.getUserId();
		        if (userId == null) {
		            throw new IllegalStateException("UserId is not set.Make sure CreateUser runs before patchUpdateUser");
		        }
		  
		        String finalEndpoint = endpoint.replace("{{validUserId}}", String.valueOf(userId));
		        String requestBody = APIHelperClass.convertUserToJson(user);

		        RequestSpecification spec = BaseTest.getRequestWithBasicAuth();

		        System.out.println("Sending PATCH to: " + finalEndpoint);
		        System.out.println("PATCH Body: " + requestBody);

		        return spec.body(requestBody).patch(finalEndpoint);
		    }
		}



