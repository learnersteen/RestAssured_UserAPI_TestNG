package utils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestCaseLoader {
	
	
	 public static List<Map<String, Object>> loadTestCases(String fileName) throws Exception {
	        ObjectMapper mapper = new ObjectMapper();

	        InputStream is = TestCaseLoader.class.getClassLoader().getResourceAsStream(fileName);

	        if (is == null) {
	            throw new RuntimeException("Resource not found: " + fileName);
	        }

	        return mapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
	    }

	    // Method used by DataProvider - converts List of Maps to Object[][]
	    public static Object[][] loadTestCasesAsDataProvider(String fileName) throws Exception {
	        List<Map<String, Object>> testCases = loadTestCases(fileName);
	        Object[][] data = new Object[testCases.size()][1];

	        for (int i = 0; i < testCases.size(); i++) {
	            data[i][0] = testCases.get(i);
	        }

	        return data;
	    }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
//	 public static List<Map<String, Object>> loadTestCases(String fileName) throws Exception {
//	        ObjectMapper mapper = new ObjectMapper();
//	        InputStream is = TestCaseLoader.class.getClassLoader().getResourceAsStream(fileName);
//	        return mapper.readValue(is, new TypeReference<List<Map<String, Object>>>(){});
//	    }
//	 
//	 //Method used by DataProvider
//	    public static Object[][] loadTestCasesAsDataProvider(String fileName) throws Exception {
//	        List<Map<String, Object>> testCases = loadTestCases(fileName);
//	        Object[][] data = new Object[testCases.size()][1];
//
//	        for (int i = 0; i < testCases.size(); i++) {
//	            data[i][0] = testCases.get(i);
//	        }
//
//	        return data;
//	    }
//	}
//	
//
//	
//	public static Object[][] loadTestCasesAsDataProvider(String fileName) throws Exception {
//	    List<Map<String, Object>> testCases = loadTestCases(fileName);
//	    Object[][] data = new Object[testCases.size()][1];
//	    for (int i = 0; i < testCases.size(); i++) {
//	        data[i][0] = testCases.get(i);
////	    }
//	    return data;
//	}
//
//	public static List<Map<String, Object>> loadTestCases(String fileName) throws Exception {
//	    ObjectMapper mapper = new ObjectMapper();
//	    InputStream is = TestCaseLoader.class.getClassLoader().getResourceAsStream(fileName);
//	    return mapper.readValue(is, new TypeReference<List<Map<String, Object>>>(){});
//	}
//}