package tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utils.ConfigReader;

import static io.restassured.RestAssured.given;

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


public class HTTPRequests {
	
	void getUsers()
	
	{
		given()
		.when()
			.get("https://reqres.in/api/users/2")
		.then()
		    .statusCode(200)
		    .body("page",equalTo(2))
		    .log().all();
	}
	

}
