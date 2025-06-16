package apirequesthandlers;

import java.util.Map;
import hooks.Hooks;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.APIHelperClass;

public class GetUserByUserIdGETRequest {
		
		public static Response sendGetByUserIdRequest(Map<String, Object> testCase) throws Exception {

			String endpoint = (String) testCase.get("endpoint");
			String finalendpoint = endpoint.replace("{{validUserId}}", String.valueOf(APIHelperClass.getUserId()));
		            
			 RequestSpecification spec = Hooks.validAuthAndHeader();
			 
			 System.out.println("Sending GET by UserId request to endpoint: " + finalendpoint);
			 
			 
			 return spec.get(finalendpoint);

		}
	


}
