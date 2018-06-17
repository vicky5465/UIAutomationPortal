package scenarios;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import report.ReportManager;
import report.TestResult;

import configuration.ConfigData;
import data.TestData;
import data.TestPreparation;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import pageobjects.PageObjects;

//import utility.TestResult;



public class BaseTest {

	
//		protected ITestContext thiscontext;
	protected Map<String, String> config;
	protected ConfigData configData;
	protected TestData testData;
	protected WebDriver driver;
	protected CommonScenarios scenario;
	protected PageObjects pObject;
//	public List<String> testNames = new ArrayList<String>();
//	public TestName name;
	public String testName;
	

	@BeforeTest
	public void setupTest(ITestContext context) {
//		this.thiscontext = context;
		//get the testName from XML file, <test name="ClientListSuite_web">
		testName = context.getName();
//		System.out.println(cont);
//		System.out.println("test name is: " + testName);
//		Reporter.log("LOG1 - Platform: " + testName, true);
		//to init report during test start
		init(testName);
		
	}

	@BeforeClass
	public void setupClass(ITestContext context) {	
//	public void setupClass() {	
		//initiate the testData
		testData = new TestData();
//		//initiate config data
		configData = new ConfigData(context.getName());
////		String testName = name.getTestName();
//		System.out.println("test name in before class is: " + testName);
//		configData = new ConfigData(testName);
		//initiate drivers
		driver = TestPreparation.startPreparation(configData);
		System.out.println("start setting up the scenario");
//		DriverFixtures fixture = new DriverFixtures(driver);
		pObject = new PageObjects();
		PageFactory.initElements(new AppiumFieldDecorator(driver, 5, TimeUnit.SECONDS), pObject);
		

		//initiate scenarios
//		scenario = TestPreparation.getScenarios(configData,driver);
		scenario = TestPreparation.getScenarios(configData, driver, pObject);
		System.out.println("finish setting up");
	}

	private void init(String name) {		
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		//to get the current time and set as the test start time
//		long start = System.currentTimeMillis();
//		System.out.println("test start time is: " + start);
//		long end = System.currentTimeMillis();
//		System.out.println("test end time is: " + end);

		//		TestResult result = new TestResult(name, host, start, end, null);
		//		ReportManager.setCurrentTestResult(result);

	}	

	//	@Test
	//	public void testFail() throws InterruptedException{
	//		Thread.sleep(5000);
	//		Reporter.log("LOG2 - test fail",true);
	//		Assert.assertEquals(configData.getDevice(),"ios");
	//	}
	//
	//	@Test(enabled = false)
	//	public void testSkip() throws InterruptedException{
	//		Thread.sleep(5000);
	//	}

	//	@Test
	//	public void testPass(){
	//		try {
	//			Thread.sleep(10000);
	//		} catch (InterruptedException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		Assert.assertEquals(add(1,1), 2);
	//	}
	//
	//	public int add(int a, int b){
	//		Reporter.log("LOG3 - test add function");
	//		return a+b;
	//	}

	@BeforeMethod
	public void beforeMehtod(Method method) {
		//		library.setCurrentTestMethod(method.getName());
		//get the specific scenario

	}

	@AfterClass
	public void cleanUp() {
		System.out.println("closing");
		System.out.println(Reporter.getCurrentTestResult().getStatus());
		if (scenario != null) {
			System.out.println("waiting");
//			scenario.logOut();
			String deviceID = configData.getUDID();
			scenario.quit(deviceID);
		}

	}

	@AfterMethod
	public void getScreenshot(){
		//			Reporter.getCurrentTestResult().getParameters();
//		for(Object out : Reporter.getCurrentTestResult().getParameters()){
//			String test = Reporter.getCurrentTestResult().getName();
//			System.out.println("Param for test: " + test + " is: " + out);
//		}
		System.out.println("after test method");
		System.out.println(Reporter.getCurrentTestResult().getStatus());
		
		if (Reporter.getCurrentTestResult().getStatus()==ITestResult.FAILURE){//ITestResult.FAILURE) { 
//			System.out.println("skip result: " + ITestResult.SKIP);
//			System.out.println("fail result: " + ITestResult.FAILURE);
//			System.out.println("pass result: " + ITestResult.SUCCESS);
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); 
			try {
				FileUtils.copyFile(scrFile, new File("screenshots/"+System.currentTimeMillis()+".png"));
				System.out.println("failed case screenshot is saved");
			} catch (IOException e) {
				e.printStackTrace();
			} 
		} 
		

	}

}
