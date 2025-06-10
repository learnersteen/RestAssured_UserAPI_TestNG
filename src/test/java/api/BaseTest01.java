package api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;
import utils.ExtentManager;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import com.aventstack.chaintest.plugins.ChainTestListener;

@Listeners(ChainTestListener.class)
public class BaseTest01 {
	
	protected boolean includeAuth = true;
	protected boolean includeHeaders = true;
	
	
	static {
        String baseURI = ConfigReader.get("baseURI");
        if (baseURI == null || baseURI.isEmpty()) {
            throw new IllegalStateException("Base URI is not configured correctly!");
        }
        RestAssured.baseURI = baseURI;
    }
	
	
	@BeforeClass
	public void setup() {
	    RestAssured.baseURI = ConfigReader.get("baseURI");
	}
	
	//valid Auth and header 
	public static RequestSpecification getRequestWithBasicAuth() {
        return RestAssured
                .given()
                .auth().preemptive()
                .basic(ConfigReader.get("username"), ConfigReader.get("password"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");
    }
	
	//missing auth
	 public static RequestSpecification getRequestWithoutAuth() {
	        return RestAssured
	                .given()
	                .header("Accept", "application/json")
	                .header("Content-Type", "application/json");
	    }
	 
	 //invalid auth 
	 public static RequestSpecification getRequestWithInvalidAuth() {
	        return RestAssured
	                .given()
	                .auth().preemptive()
	                .basic("invalidUser", "invalidPass")
	                .header("Accept", "application/json")
	                .header("Content-Type", "application/json");
	    }
	 
	 //missing header
	 public static RequestSpecification getRequestWithoutHeaders() {
	        return RestAssured
	                .given()
	                .auth().preemptive()
	                .basic(ConfigReader.get("username"), ConfigReader.get("password"));
	    }
	 
	 //invalid header
	 public static RequestSpecification getRequestWithInvalidHeaders() {
	        return RestAssured
	                .given()
	                .auth().preemptive()
	                .basic(ConfigReader.get("username"), ConfigReader.get("password"))
	                .header("Accept", "application/xml")       // expecting JSON, but sending XML
	                .header("Content-Type", "text/plain");     // wrong content type
	    }
	
	 @AfterSuite(alwaysRun = true)
	    public void tearDownSuite() {
	        System.out.println("Flushing Extent Reports...");
	        ExtentManager.getInstance().flush();
	    }
	}


