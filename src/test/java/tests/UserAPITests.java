package tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utils.ConfigReader;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


/*
given()
  content type, set cookies, add param, set headers info etc.,

when()
    get, put, post, patch, delete

then()
    validate status code, extract response, extract header cookies and response body..
   
*/

public class UserAPITests {
	
	@BeforeClass
    public void setup() {
        ConfigReader.loadConfig(); 
               
    }
	
	{
		given()
			.baseUri(ConfigReader.get("baseURI"))                    
			.auth().preemptive()
			.basic(ConfigReader.get("username"), ConfigReader.get("password"));
		
	
	@Test
    public void getAllUsers() {
				
		
			.when()
				.get("https://userserviceapp-f5a54828541b.herokuapp.com/uap/users")
			.then()
			    .statusCode(200)
			    //.body("page",equalTo(2))
			    .log().all();
		}
	}
		
//		given()
//			.baseUri(ConfigReader.get("baseURI"))
//			.auth().preemptive().basic(ConfigReader.get("username"), ConfigReader.get("password"));
//			//.header(ConfigReader.get("customHeaderName"), ConfigReader.get("customHeaderValue"))
//	       // .contentType(ConfigReader.get("contentType"));
//		when()
//		   .get("https://userserviceapp-f5a54828541b.herokuapp.com/uap/createusers");
//		then()
//			.statusCode("expectedStatusCode");
//			.statusResponse
//			
//		
//	}
	



	
}