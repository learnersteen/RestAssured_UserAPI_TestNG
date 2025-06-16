package apirequesthandlers;

import java.util.Map;

import hooks.Hooks;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.UserDetails;
import utils.APIHelperClass;

public class CreateUserPOSTRequest {

    public static Response sendPostRequest(Map<String, Object> testCase) throws Exception {

        UserDetails user = APIHelperClass.buildUserFromMap(testCase);
        String endpoint = (String) testCase.get("endpoint");
        String requestBody = APIHelperClass.convertUserToJson(user);

        RequestSpecification spec = Hooks.validAuthAndHeader();

        System.out.println("Sending POST request to endpoint: " + endpoint);
        System.out.println("POST Request Body: " + requestBody);

        return spec.body(requestBody).post(endpoint);
    }
   

}
