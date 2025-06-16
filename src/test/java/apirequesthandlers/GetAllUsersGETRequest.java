package apirequesthandlers;

import java.util.Map;

import hooks.BaseTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetAllUsersGETRequest {
		
		public static Response sendGetAllUsersRequest(Map<String, Object> testCase) throws Exception {

			String endpoint = (String) testCase.get("endpoint");
			            
		   RequestSpecification spec = BaseTest.getRequestWithBasicAuth();
			 
			 System.out.println("Sending GETAllUsers request to endpoint: " + endpoint);
			 
			 
			 return spec.get(endpoint);

		}
	
}
