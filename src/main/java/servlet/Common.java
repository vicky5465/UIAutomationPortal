package servlet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.WebDriver;

import configuration.ConfigData;
import pageobjects.PageObjects;
import scenarios.CommonScenarios;
import data.TestData;

public class Common {
	/**
	 * this function is used to export the test methods under each test cases to the website 
	 * @param testCase
	 * @return
	 */
	public static List<Method> getTestMethod(String testCase){
		List<Method> testMethods = new ArrayList<Method>();

		try {
			//to get class of the test case
			Class clazz = Class.forName(testCase);
			System.out.println(clazz);
			//to create an instance of the constructor with no param
			Object obj = clazz.newInstance();
			//to get all the methods under the class
			Method[] m = clazz.getDeclaredMethods();
			//iterate the method[]
			for(int i=0; i<m.length;i++){
				Method testMethod = m[i];
				System.out.println(testMethod);
				if(testMethod.isAnnotationPresent(org.testng.annotations.Test.class)){
					testMethods.add(testMethod);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return testMethods;
	}

	public static List<Method> getAnnotatedMethod(String className, Class<? extends Annotation> annoName){
		List<Method> testMethods = new ArrayList<Method>();
		try {
			//to get class of the test case
			Class clazz = Class.forName(className);
			System.out.println(clazz);
			//to create an instance of the constructor with no param
			Object obj = clazz.newInstance();
			//to get all the methods under the class
			Method[] m = clazz.getDeclaredMethods();
			//iterate the method[]
			for(int i=0; i<m.length;i++){
				Method testMethod = m[i];
				System.out.println(testMethod);
				if(testMethod.isAnnotationPresent(annoName)){
					testMethods.add(testMethod);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return testMethods;
	}

	public static Map<String, Object> getTestNGTesetData(Class dataProviderClass, String dataProvider) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Object obj = dataProviderClass.newInstance();
//			Field isFromWebField = dataProviderClass.getDeclaredField("isFromWeb");
//			
//			isFromWebField.setAccessible(true);
//			
//			isFromWebField.set(obj, "true");
			
			Method[] m = dataProviderClass.getDeclaredMethods();
			for(int i=0; i<m.length; i++){
				Method method = m[i];
				String dataProviderName = method.getAnnotation(org.testng.annotations.DataProvider.class).name();
				if(dataProviderName.equals(dataProvider)){
					System.out.println("this is the test method! "  + method);
					//					data = (Map<String, Object>) method.invoke(obj, null);[[Ljava.lang.Object; cannot be cast to java.util.Map
					Object[][] object = (Object[][]) method.invoke(obj,null);
					data = conver2DArraysToMap(object);
					//					System.out.println(object.length);
					//					System.out.println(object["Username"][]);

//					System.out.println(data.get("Username"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return data;
	}


	/**
	 * this is used to run methods with @Test under each test file
	 * @param testCase
	 * @param ngData
	 */
	public static String runTest(String testCase, TestCaseDataFromTestNG ngData){
		String result = "";
		try {

			//to get class of the test case
			Class clazz = Class.forName(testCase);
			//to get the super class
			Class superClass = clazz.getSuperclass();

			//to get the config data returned after running the @BeforeClass
			ConfigData cData = ngData.getConfigData();
			TestData tData = ngData.getTestData();
			WebDriver driverFromNG = ngData.getDriver();
			CommonScenarios scenario = ngData.getScenario();
			PageObjects pageObject = ngData.getpObject();
			
			//to create an instance of the constructor with no param
			Object obj = clazz.newInstance();
			
			//to set scenario fields
			Field scenarioField = superClass.getDeclaredField("scenario");
			scenarioField.setAccessible(true);
			scenarioField.set(obj, scenario);
			
			//to set configData
			Field configDataField = superClass.getDeclaredField("configData");
			configDataField.setAccessible(true);
			configDataField.set(obj, cData);

			//to set testData
			Field testDataField = superClass.getDeclaredField("testData");
			testDataField.setAccessible(true);
			testDataField.set(obj, tData);

			//to set driver
			Field driverField = superClass.getDeclaredField("driver");
			driverField.setAccessible(true);
			driverField.set(obj, driverFromNG);

			//to set pObject
			Field pageObjectField = superClass.getDeclaredField("pObject");
			pageObjectField.setAccessible(true);
			pageObjectField.set(obj, pageObject);
			
			//to get all the methods under the class
			Method[] m = clazz.getDeclaredMethods();
			//iterate the method[]
			for(int i=0; i<m.length;i++){
				Method testMethod = m[i];
				//just invoke the method with @Test
				if(testMethod.isAnnotationPresent(org.testng.annotations.Test.class)){
					Class dataProviderClass = testMethod.getAnnotation(org.testng.annotations.Test.class).dataProviderClass();
					String dataProvider = testMethod.getAnnotation(org.testng.annotations.Test.class).dataProvider();		
					System.out.println("dataProviderClass is: " + dataProviderClass);
					System.out.println("dataProviderClass is: " + dataProvider);
									
					//to create the data for the parameter of the method
					//@DataProvider function
					Map<String, Object> data = new HashMap<String, Object>();
					data = Common.getTestNGTesetData(dataProviderClass,dataProvider);
					if(data!=null){
						//before invoke the test method, we need to invoke the methods in the BaseTest
						//to trigger the method
						m[i].invoke(obj, data);
					}else{
						System.out.println("it has no data");
					}		
				}
			}
			result = testCase + " is passed!";
		}catch (Exception  e) {
			result = e.toString();
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args){
		String testCase = "testSuites.AgentApp.Login.TestPALogin";
		List<Method> methods = getTestMethod(testCase);
		for(Method method : methods){
			System.out.println(method);
		}

		Class clazz = TestData.class;
		String data = "TLLoginTestData";
		getTestNGTesetData(clazz,data);
	}

	
	
	/**
	 * this function used to convert the return value of function with @DataProvided in TestData.java to Map
	 * @param obj
	 * @return
	 */
	private static Map<String, Object> conver2DArraysToMap(Object[][] obj){
		Map<String, Object> map = new HashMap<String, Object>(obj.length);
		for (Object[] mapping : obj)
		{
			//as in the @DataProvider function, we just convert map to 1D array of the required 2D arraylists
			//so just get the first dimensional of the return value
			map = (Map<String, Object>) mapping[0];
		}
		return map;
	}

	/**
	 * this function is used to run the fucntion with @BeforeClass in BaseTest.java
	 * @param testName
	 * @param platform
	 * @return
	 */
	public static TestCaseDataFromTestNG parseBaseTestBeforeClass(String testName, String platform, String browser, String env, String email, String password) {
		//to initiate an object of data from testng
		TestCaseDataFromTestNG ngData = new TestCaseDataFromTestNG();
		try {

			//to get the class of BaseTest
			Class clazz = Class.forName("com.movoto.scenarios.BaseTest");
			Object obj = clazz.newInstance();
			System.out.println(clazz.getName());

			//to get the test name field
			Field f = clazz.getDeclaredField("testName");
			System.out.println("before set Field name is: " + f.get(obj));
			String value = testName + "_" +platform;	
			//to set the test name
			//from web, we use reflection to parse the testng test function
			//so we have to manually pass the data which is passed by xml under TestNG
			f.set(obj,value);
			System.out.println("after set Field name is: " + f.get(obj));
			//to get the method under @BeforeClass
			//this function is only parst the BeforeClass in TestNG
			Class<? extends Annotation> anno = org.testng.annotations.BeforeClass.class;
			List<Method> methods = Common.getAnnotatedMethod(clazz.getName(), anno);
			//to invoke the method with annotation = BeforeClass
			for (Method method : methods){
				System.out.println(method);
				method.invoke(obj, null);
				//after the method is invoked, set the testing data to methods with @Test

				//to get and set scenario
				Field scenario = clazz.getDeclaredField("scenario");
				scenario.setAccessible(true);
				CommonScenarios commonScenario = (CommonScenarios) scenario.get(obj);
				System.out.println("scenario field is: " + scenario.get(obj));	
				ngData.setScenario(commonScenario);

				//to get and set configData
				Field configDataField = clazz.getDeclaredField("configData");
				configDataField.setAccessible(true);
				ConfigData configData = (ConfigData) configDataField.get(obj);
				System.out.println("configData field is: " + configDataField.get(obj));	
				ngData.setConfigData(configData);

				//to get and set testData
				Field testDataField = clazz.getDeclaredField("testData");
				testDataField.setAccessible(true);
				TestData testData = (TestData) testDataField.get(obj);
				System.out.println("testData field is: " + testDataField.get(obj));	
				ngData.setTestData(testData);


				//to get and set driver
				Field driverField = clazz.getDeclaredField("driver");
				driverField.setAccessible(true);
				WebDriver driver = (WebDriver) driverField.get(obj);
				System.out.println("driver field is: " + driverField.get(obj));	
				ngData.setDriver(driver);

				//to get and set pObject
				Field pageObjectField = clazz.getDeclaredField("pObject");
				pageObjectField.setAccessible(true);
				PageObjects pObject = (PageObjects) pageObjectField.get(obj);
				System.out.println("pageObject field is: " + pageObjectField.get(obj));	
				ngData.setpObject(pObject);

				System.out.println(ngData);


			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ngData;
	}

	/**
	 * this function is used to run the funtion with @AfterClass in BaseTest.java file
	 * @param ngData
	 */
	public static void parseBaseTestAfterClass(TestCaseDataFromTestNG ngData) {
		try {
			Class clazz = Class.forName("com.movoto.scenarios.BaseTest");
			Object obj = clazz.newInstance();
			//to set the scenario
			CommonScenarios scenario = ngData.getScenario();
			Field field = clazz.getDeclaredField("scenario");
			field.setAccessible(true);
			field.set(obj, scenario);
			
			ConfigData cData = ngData.getConfigData();
			Field cDataField = clazz.getDeclaredField("configData");
			cDataField.setAccessible(true);
			cDataField.set(obj, cData);
			System.out.println("scenario field name is: " + field.getName() + "..." + field.get(obj));
			//to get the method under @BeforeClass
			 Class<? extends Annotation> anno = org.testng.annotations.AfterClass.class;
			List<Method> methods = Common.getAnnotatedMethod(clazz.getName(), anno);
			for (Method method : methods){
				System.out.println(method);
				method.invoke(obj, null);
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}


