package apirequesthandlers;

import java.util.Map;

import hooks.BaseTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.APIHelperClass;

public class DeleteUserByUserFirstNameDELETERequest {
	
public static Response sendDeleteByUserFirstNameRequest(Map<String, Object> testCase) throws Exception {

	String endpoint = (String) testCase.get("endpoint");
	String finalendpoint = endpoint.replace("{{validUserFirstName}}", APIHelperClass.getUserFirstname());
            
	 RequestSpecification spec = BaseTest.getRequestWithBasicAuth();
	 
	 System.out.println("Sending DELETE request to endpoint: " + finalendpoint);
	 
	 
	 return spec.delete(finalendpoint);

}

}