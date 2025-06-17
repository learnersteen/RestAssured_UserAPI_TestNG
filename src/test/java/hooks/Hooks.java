package hooks;

import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import com.aventstack.chaintest.plugins.ChainTestListener;

import utils.ConfigReader;

import static io.restassured.RestAssured.given;

@Listeners(ChainTestListener.class)
public class Hooks {

        protected RequestSpecification requestSpec;

        @BeforeClass(alwaysRun = true)
        public void setupBaseRequestSpec() {
            RestAssured.baseURI = ConfigReader.get("baseURI");

            requestSpec = validAuthAndHeader(); // you can assign like this
        }

        public static RequestSpecification validAuthAndHeader() {
            return given()
                    .auth().preemptive()
                    .basic(ConfigReader.get("username"), ConfigReader.get("password"))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json");
        }
    }