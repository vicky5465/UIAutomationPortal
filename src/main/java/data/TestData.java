package data;

//import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import utils.file.FileUtility;
import utils.file.JSONManager;


public class TestData {
	static Boolean isFromWeb = false;
	//this is used for parameterized testing
	//to avoid anyone still using this data provider
	//	@DataProvider(name = "LoginTestData")
	//	public static Object[][] LoginDataProvider(ITestContext context, Method method) throws Exception {
	//		//		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	//		//		library.setCurrentTestMethod(method.getName());
	//		FileUtility fileUtil = new FileUtility();
	//		String path = "data/LoginTest.xlsx";
	//		fileUtil.openExcelSheet(path, "Login", "read");
	//
	//		int rowCount = fileUtil.getExcelRowCount();
	//		//		System.out.println("row count is: " + rowCount);
	//		Object[][] data = new Object[rowCount - 1][1];
	//		Map<String, Object> map = new HashMap<>();
	//		map.put("RowCount", rowCount);
	//		for (int i = 0; i < rowCount - 1; i++) {		
	//			int j = i + 1;
	//			String userName = fileUtil.getFromExcelRowAndColumn(j, "UserName");
	//			String password = fileUtil.getFromExcelRowAndColumn(j, "Password");
	//			String agentName = fileUtil.getFromExcelRowAndColumn(j, "AgentName");
	//			String baseURL = fileUtil.getFromExcelRowAndColumn(j, "BaseURL");
	//			String role = fileUtil.getFromExcelRowAndColumn(j, "Role");
	//
	//
	//			map.put("Username"+j, userName);
	//			map.put("Password"+j, password);
	//			map.put("AgentName"+j, agentName);
	//			map.put("BaseURL"+j, baseURL);
	//			map.put("Role"+j, role);
	//
	//
	//			String jsonFilePath = "data/AgentLoginData.json";
	//			JSONManager.writeLogin(userName, password, jsonFilePath);
	//			Map<String, Object> agentLoginData = JSONManager.convertJsonDataToMap(jsonFilePath);
	//			map.put("AgentLoginData"+j, agentLoginData);
	//
	//			try {
	//				data[i][0] = map;
	//			} catch (Exception e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//			//			System.out.println(map);
	//		}
	//		fileUtil.closeExcelSheet(path, "Login", "read");
	//		//		System.out.println(map);
	//		Object[][] obj = { { map } };
	//
	//		return obj;
	//	}

	//this function only support single testing account
	//this function will used for any login with PA agent
	//no matter the request is from web or testng
	@DataProvider(name = "TMLoginTestData")
	public static Object[][] TMLoginDataProvider() throws Exception {
		System.out.println("this request is from web? " + isFromWeb);
		Map<String, Object> map = new HashMap<>();
		if(!isFromWeb){
			FileUtility fileUtil = new FileUtility();
			//in this sheet, one TL, one TM, one PA account is provided
			//for different kind of agent testing, it will get differnt data
			String path = "data/Login.xlsx";
			fileUtil.openExcelSheet(path, "Login", "read");

			//to get the json file path
			//used to write the testing account payload to call the related api
			String jsonFilePath = "data/AgentLoginData.json";
			map.put("jsonFilePath", jsonFilePath);

			//to get the data for TM
			String userNameTM = fileUtil.getFromExcelRowAndColumn(2, "UserName");
			String passwordTM = fileUtil.getFromExcelRowAndColumn(2, "Password");
			String agentNameTM = fileUtil.getFromExcelRowAndColumn(2, "AgentName");
			String baseURL = fileUtil.getFromExcelRowAndColumn(2, "BaseURL");
			String roleTM = fileUtil.getFromExcelRowAndColumn(2, "Role");
			String searchTextTM = fileUtil.getFromExcelRowAndColumn(2, "SearchText");
			String sfUserName = fileUtil.getFromExcelRowAndColumn(2, "SFUserName");
			String sfUserPassword = fileUtil.getFromExcelRowAndColumn(2, "SFPassword");

			//to close the sheet
			fileUtil.closeExcelSheet(path, "Login", "read");

			//put the TM data into map
			map.put("Username", userNameTM);
			map.put("Password", passwordTM);
			map.put("AgentName", agentNameTM);
			map.put("Role", roleTM);
			map.put("BaseURL", baseURL);
			map.put("SearchText", searchTextTM);
			map.put("usernameSF", sfUserName);
			map.put("passwordSF", sfUserPassword);

			JSONManager.writeLogin(userNameTM, passwordTM, jsonFilePath);
			Map<String, Object> agentLoginData = JSONManager.convertJsonDataToMap(jsonFilePath);
			map.put("AgentLoginData", agentLoginData); 
		}else{
			//need to read the web input to the excel LoginDataFromWeb
		}

		Object[][] obj = { { map } };

		return obj;
	}


}
