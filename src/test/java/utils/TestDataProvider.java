package utils;



import org.testng.annotations.DataProvider;

public class TestDataProvider {
	
		
		@DataProvider(name = "createUserPOSTTestData")
		public static Object[][] postUserData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("POST_All_Tests.json");
		}
		
		@DataProvider(name = "beforeMethodCreateUser")
		public static Object[][] createUserTestData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("post_user_data_Latest-trial.json");
		}
		
		@DataProvider(name = "UpdateUserPUTTestData")
		public static Object[][] putUserData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("PUT_All_Tests.json");
		}
		
			
		@DataProvider(name = "UpdateUserPATCHTestData")
		public static Object[][] patchUserData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("PATCH_All_Tests.json");
		}
		
		@DataProvider(name = "GetUserByUserIdTestData")
		public static Object[][] getUserDataByUserId() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("getByUserId_data_Latest-trial.json");
		}
		
		
		@DataProvider(name = "GetUserByFirstNameTestData")
		public static Object[][] getUserByUserFirstNameData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("getByUserFirstName_data_Latest-trial.json");
		}
		

		@DataProvider(name = "GetAllUsersTestData")
		public static Object[][] getAllUsersData() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("get_all_users_Latest-trial.json");
		}
	

		
		@DataProvider(name = "DeleteByUserIdTestData")
		public static Object[][] deleteUserDataByUserId() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("deleteByUserId_data_Latest-trial.json");
		}
		

		@DataProvider(name = "DeleteUserByUserFirstNameData")
		public static Object[][] deleteUserDataByUserFirstName() throws Exception {
		    return TestCaseLoader.loadTestCasesAsDataProvider("deleteByUserFirstName_data_Latest-trial.json");
		}

		


		}

	
