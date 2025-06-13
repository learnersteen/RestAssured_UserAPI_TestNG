//package tests;
//
//import java.util.List;
//import java.util.Map;
//
//import org.testng.annotations.Test;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import api.BaseTest;
//import pojo.UserAddress;
//import pojo.UserDetails;
//import utils.TestCaseLoader;
//
//public class UserAPITests01 extends BaseTest {
//	
//	@Test
//	public void userAPITests() throws Exception {
//		
//		List<Map<String, Object>> testCases = TestCaseLoader.loadTestCases("post_put_patch_delete_get.json");
//		
//		ObjectMapper mapper = new ObjectMapper(); // Use Jackson to map nested userAddress
//		
//        for (Map<String, Object> testCase : testCases) {
//            String method = (String) testCase.get("method");
//            String endpoint = (String) testCase.get("endpoint");
//            int expectedStatusCode = (int) testCase.get("expectedStatusCode");
//            
//         // Set UserDetails
//            UserDetails user = new UserDetails();
//            if (testCase.containsKey("userFirstName")) user.setUserFirstName((String) testCase.get("userFirstName"));
//            if (testCase.containsKey("userLastName")) user.setUserLastName((String) testCase.get("userLastName"));
//            if (testCase.containsKey("userContactNumber")) user.setUserContactNumber(Long.parseLong(testCase.get("userContactNumber").toString()));
//            if (testCase.containsKey("userEmailId")) user.setUserEmailId((String) testCase.get("userEmailId"));
//            
//            
//            UserAddress userAddress = new UserAddress();
//            if (testCase.containsKey("plotNumber")) userAddress.setPlotNumber((String) testCase.get("plotNumber"));
//            if (testCase.containsKey("street")) userAddress.setStreet((String) testCase.get("street"));
//            if (testCase.containsKey("state")) userAddress.setState((String) testCase.get("state"));
//            if (testCase.containsKey("country")) userAddress.setCountry((String) testCase.get("country"));
//            if (testCase.containsKey("zipCode")) userAddress.setZipCode((int) testCase.get("zipCode"));
//            
//        }
//              
//        }
//        
//	}
//
//	
//
