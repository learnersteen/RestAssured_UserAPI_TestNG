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
	        return mapper.readValue(is, new TypeReference<List<Map<String, Object>>>(){});
	    }
	}


