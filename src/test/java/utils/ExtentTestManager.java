package utils;

import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void startTest(String testName, String desc) {
        ExtentTest extentTest = ExtentManager.getInstance().createTest(testName, desc);
        test.set(extentTest);
    }

    public static void endTest() {
        ExtentManager.getInstance().flush();
    }
}
