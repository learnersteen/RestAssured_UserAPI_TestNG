package utils;

import java.util.Map;

import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import pojo.UserAddress;
import pojo.UserDetails;

public class Validator {

    public void validateAllFields(JsonPath responseJson, Map<String, Object> expectedData) {
        // Your existing method for JsonPath + Map validation
    }

    // New method for POJO to POJO validation
    public void validateAllFields(UserDetails actual, UserDetails expected) {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(actual.getUserFirstName(), expected.getUserFirstName(), "Incorrect userFirstName");
        softAssert.assertEquals(actual.getUserLastName(), expected.getUserLastName(), "Incorrect userLastName");
        softAssert.assertEquals(actual.getUserContactNumber(), expected.getUserContactNumber(), "Incorrect userContactNumber");
        softAssert.assertEquals(actual.getUserEmailId(), expected.getUserEmailId(), "Incorrect userEmailId");

        UserAddress actualAddress = actual.getUserAddress();
        UserAddress expectedAddress = expected.getUserAddress();

        softAssert.assertEquals(actualAddress.getPlotNumber(), expectedAddress.getPlotNumber(), "Incorrect plotNumber");
        softAssert.assertEquals(actualAddress.getStreet(), expectedAddress.getStreet(), "Incorrect street");
        softAssert.assertEquals(actualAddress.getState(), expectedAddress.getState(), "Incorrect state");
        softAssert.assertEquals(actualAddress.getCountry(), expectedAddress.getCountry(), "Incorrect country");
        softAssert.assertEquals(actualAddress.getZipCode(), expectedAddress.getZipCode(), "Incorrect zipCode");

        softAssert.assertAll();
    }
}
