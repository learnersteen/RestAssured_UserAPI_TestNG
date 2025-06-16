package apirequesthandlers;

import java.util.Map;

import hooks.BaseTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.APIHelperClass;

public class DeleteUserByFirstNameDELETERequest {
	
	
	public static Response sendDeleteUserByFirstNameRequest(Map<String, Object> testCase) throws Exception {

		String endpoint = (String) testCase.get("endpoint");
		String finalendpoint = endpoint.replace("{{validUserFirstName}}", String.valueOf(APIHelperClass.getUserFirstname()));
	            
		 RequestSpecification spec = BaseTest.getRequestWithBasicAuth();
		 
		 System.out.println("Sending GET by UserFirstName request to endpoint: " + finalendpoint);
		 
		 
		 return spec.delete(finalendpoint);

	}


}
