package tests;

import org.testng.annotations.Test;

import api.BaseTest;
import api.UserPayload;

//import api.base.BaseTest;
//import api.payload.UserPayload;
//import io.restassured.http.ContentType;
//import org.testng.annotations.Test;
//
//import static io.restassured.RestAssured.*;
//import static org.hamcrest.Matchers.*;

public class UserTests extends BaseTest {

    static int createdUserId;

    @Test(priority = 1)
    public void createUser() {
        UserPayload user = new UserPayload();
        user.setUserFirstName("Lavanya");
        user.setUserLastName("Y");
        user.setUserContactNumber(9876543210L);
        user.setUserEmailId("lavanya@example.com");

        createdUserId = given()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post("/uap/createusers")
        .then()
            .statusCode(201)
            .extract()
            .path("userId");

        System.out.println("User created with ID: " + createdUserId);
    }

  
    }

