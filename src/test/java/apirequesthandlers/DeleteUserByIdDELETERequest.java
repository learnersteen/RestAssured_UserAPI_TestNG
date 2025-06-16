package apirequesthandlers;

import java.util.Map;

import hooks.Hooks;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.APIHelperClass;

public class DeleteUserByIdDELETERequest {
	
	public static Response sendDeleteByUserIdRequest(Map<String, Object> testCase) throws Exception {

		String endpoint = (String) testCase.get("endpoint");
		String finalendpoint = endpoint.replace("{{validUserId}}", String.valueOf(APIHelperClass.getUserId()));
	            
        RequestSpecification spec = Hooks.validAuthAndHeader();
		 
		 System.out.println("Sending DELETE by UserId request to endpoint: " + finalendpoint);
		 
		 
		 return spec.delete(finalendpoint);

	}

    RequestSpecification spec = Hooks.validAuthAndHeader();
}
