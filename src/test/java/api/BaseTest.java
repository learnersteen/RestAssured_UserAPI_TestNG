package api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;
import org.testng.annotations.BeforeClass;

public class BaseTest {
	
	@BeforeClass
	public void setup() {
		RestAssured.baseURI = ConfigReader.get("baseURI");
	}
    
	public static RequestSpecification getRequestWithBasicAuth() {
        return RestAssured
                .given()
                .auth().preemptive()
                .basic(ConfigReader.get("username"), ConfigReader.get("password"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");
    }
}


