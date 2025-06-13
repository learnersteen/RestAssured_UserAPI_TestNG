package utils;

import java.util.Map;

import org.testng.annotations.DataProvider;

public class TestDataProvider {
	
		
		@DataProvider(name = "postUserData")
		public static Object[][] postUserData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("post_user_data_Latest-trial.json");
		}
		
		@DataProvider(name = "postUserData1")
		public static Object[][] postUserData1() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("post2_user_data_Latest.json");
		}
		
		@DataProvider(name = "putUserData")
		public static Object[][] putUserData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("put_user_data_Latest-trial.json");
		}
		
		@DataProvider(name = "patchUserData")
		public static Object[][] patchUserData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("patch_user_data_Latest-trial.json");
		}
		
		@DataProvider(name = "getUserByIdData")
		public static Object[][] getUserDataByUserId() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("getByUserId_data_Latest-trial.json");
		}
		

		
		@DataProvider(name = "DeleteUserByIdData")
		public static Object[][] deleteUserDataByUserId() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("deleteByUserId_data_Latest-trial.json");
		}
		
//		@DataProvider(name = "DeleteUserByUserFirstNameData")
//		public static Object[][] deleteUserDataByUserFirstName() throws Exception {
//		    return TestCaseLoader.loadTestCasesAsDataProvider("deleteByUserFirstName_data_Latest-trial.json");
//		}


		
		@DataProvider(name = "GetAllUsersData")
		public static Object[][] getAllUsersData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("get_all_users_Latest-trial.json");
		}
		
		@DataProvider(name = "GetUserByUserFirstNameData")
		public static Object[][] getUserByUserFirstNameData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("getByUserFirstName_data_Latest-trial.json");
		}


		}

	
