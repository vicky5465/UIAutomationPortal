package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import configuration.ConfigData;
import pageobjects.PageObjects;
import scenarios.BaseTest;
import scenarios.CommonScenarios;
import data.TestData;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import bsh.Console;



/**
 * this is used to generate test case
 * by post the test data collecting from site
 * @author vicky
 *
 */
@Path("/GenerateTestCase")
public class GenerateTestCase{
	
	
	@Context
	HttpServletRequest request;
	HttpServletResponse response; 
	HttpSession session;
	
	
	@POST	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateTestData() throws IOException {		
		String result ="";
		  BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		  String json = "";
	        if(br != null){
	            json = br.readLine();
	            JSONObject obj = new JSONObject(json);
	            System.out.println(obj);
	           
	            TestCaseDataFromWeb testCases = parseTestCaseData(obj);	
	            System.out.println(testCases);

	            result =  runTestCases(testCases);
	            
	        	JSONObject jsonObject = new JSONObject();
//	    		jsonObject.put("caseId", testIDs);
	    		jsonObject.put("result", result);
	    		
	    		
	    		result = jsonObject.toString();
	    	
	          
	        }
	        
//	        System.out.println(request.getParameterMap().size());
	        return Response.status(200).entity(result).build();
	}
	
	
	private String runTestCases(TestCaseDataFromWeb testData) {
		String result = "";
		String platform = testData.getTestingPlatform();
		System.out.println("platform is: " + platform);
		String browser = testData.getTestingBrowser();
		String env = testData.getTestingEnv();
		String email = testData.getTestingUser();
		String password = testData.getTestingPwd();
		List<String> cases = testData.getCaseNames();

		
		
		//to store the testing data to excel
		//WORKING ON WRITE THE DATA FROM WEB TO EXCEL, SO THAT THE DATA PROVIDER FUNCTION CAN USE THE DATA
//		writeDataToExcel(platform, browser, env, email, password);
		
		//run the case
		for(String testCase : cases){
			
			System.out.println("test case is: " + testCase);
			//to get the testing set up stuff
			TestCaseDataFromTestNG ngData = Common.parseBaseTestBeforeClass(testCase,platform,browser, env, email, password);
			//to run @Test method in the designated test case
			String msg = Common.runTest(testCase, ngData);
			if (!msg.contains("is passed")){
				result = testCase + " is failed because of this exception: " + msg;
			}else{
				result = msg;
			}
			//to run @AfterClass in BaseTest
			Common.parseBaseTestAfterClass(ngData);

		}
		return result;
//		System.out.println(co);
//		try{
//		    String s=co.readLine();
//		    System.out.println(s);
//		}catch(Exception e){
//			e.printStackTrace();
//		}

		
	}

	//based on the parameter testCase 
	//to trigger the test method under that test case



	private TestCaseDataFromWeb parseTestCaseData(JSONObject json) {
		List<String> caseNames = new ArrayList<String>();
		String testingEnv = json.getString("env");
		String testingPlatform = json.getString("platform");
		String testingBrowser = json.getString("browser");
		String testingUser = json.getString("email");
		String testingPwd = json.getString("password");
	
		JSONArray names = json.getJSONArray("name");
		System.out.println("names is: "+names);
		for(int i=0; i<names.length();i++){
			System.out.println("name is: "+names.getString(i));
			caseNames.add(names.getString(i));
		}
		TestCaseDataFromWeb data = new TestCaseDataFromWeb(caseNames,testingEnv,testingPlatform,testingBrowser,testingUser,testingPwd);
		return  data;
	}


	

	

}
