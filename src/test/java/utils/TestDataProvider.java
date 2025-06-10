package utils;

import org.testng.annotations.DataProvider;

public class TestDataProvider {
	
	@DataProvider(name = "postUserData")
	public static Object[][] postUserData() throws Exception {
	    return TestCaseLoader.loadTestCasesAsDataProvider("post_user_data.json");
	}

}
