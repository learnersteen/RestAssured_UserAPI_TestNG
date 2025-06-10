package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter("test-output/ExtentReport.html");
            reporter.config().setReportName("DSAlgo-Automation Test Report");
            reporter.config().setDocumentTitle("DSAlgo-Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Tester", "TeamAchievers");
        }
        return extent;
    }
}
