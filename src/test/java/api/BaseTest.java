package api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

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
	
//	@BeforeMethod
//    // Heroapp 503 issue workaround fix
//    public void warmUpApp() {
//        try {
//            System.out.println("Warming up Heroku app before state-changing test...");
//            RestAssured.get("https://userserviceapp-f5a54828541b.herokuapp.com/uap/users/health");
//            Thread.sleep(5000); // Wait for dyno to fully wake up
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        } catch (Exception ex) {
//            System.err.println("Failed to warm up app: " + ex.getMessage());
//        }
//    }
//	
	
}


