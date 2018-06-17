package scenarios;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;



import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import net.minidev.json.JSONArray;
import pageobjects.PageObjects;
import utils.driver.DriverFixtures;
import utils.webservice.APIManager;
import utils.webservice.APIResult;

/**
 * This scenario is used for IOS
 * @author vicky
 *
 */

public class CommonScenarios{

	DriverFixtures fixture;
	APIManager api;
	WebDriver driver;
	PageObjects pObj;
	APIResult apiResult;


	//	public CommonScenarios(){};
	public CommonScenarios(WebDriver driver, PageObjects pObj) {
		this.driver = driver;
		this.pObj = pObj;
		fixture = new DriverFixtures(driver);
		api = new APIManager();
		apiResult = new APIResult();

	}



	public CommonScenarios(DriverFixtures fixture, APIManager api, WebDriver driver, PageObjects pObj,
			APIResult apiResult) {
		super();
		this.fixture = fixture;
		this.api = api;
		this.driver = driver;
		this.pObj = pObj;
		this.apiResult = apiResult;

	}






	//during check the info on the screen, need to scroll to the next screeen
	public void scrollToNextScreen(int from, int to){
//		AppiumDriver ad = (AppiumDriver) this.driver;
//		fixture.wait(1);
//		Point start = getScreenCoordinatesInYAxis(ad,from);
//		Point end = getScreenCoordinatesInYAxis(ad,to);
//		ad.swipe(start.x,start.y,end.x,end.y,100);
//		fixture.wait(1);
	}

	//set the point to X% of the x axis and middle of the y axis
	public Point getScreenCoordinatesInXAxis(WebDriver driver, int percentage) {
		Point point = new Point(0, 0);
		//to get the window width and height
		int width = fixture.getWindowWidth(driver);
		int height = fixture.getWindowWidth(driver);

		//set the height as the half of the screen
		int y = height / 2;
		//set the width as the percentage of the screen
		float xf = width * ((float) percentage/ 100);
		int x = (int) Math.floor(xf);

		//set the point
		point.setLocation(x, y);
		return point;
	}

	//set the point to X% of the x axis and middle of the y axis
	public Point getScreenCoordinatesInYAxis(WebDriver driver, int percentage) {
		Point point = new Point(0, 0);
		//to get the window width and height
		int width = fixture.getWindowWidth(driver);
		int height = fixture.getWindowWidth(driver);

		//set the height as the half of the screen
		int x = width / 2;		
		//set the width as the percentage of the screen
		float yf = height * ((float) percentage / 100);
		int y = (int) Math.floor(yf);

		//set the point
		point.setLocation(x, y);
		return point;
	}

	//to disconnect with the device
	public void quit(String deviceID) {
		System.out.println("quiting");
		fixture.quit(deviceID);
	}




















}