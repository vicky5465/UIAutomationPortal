package scenarios;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;


import pageobjects.PageObjects;



import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import net.minidev.json.JSONArray;
import utils.driver.DriverFixtures;
import utils.webservice.APIManager;

public class CommonScenariosForAndroid extends CommonScenarios{

	DriverFixtures fixture;
	//	AndroidDriverFixture androidFixture;
	APIManager api;

	public CommonScenariosForAndroid(WebDriver driver, PageObjects pObj) {
		super(driver, pObj);
		fixture = new DriverFixtures(driver);
		api = new APIManager();
	}



	@Override
	public void scrollToNextScreen(int from, int to){
//		System.out.println("swiping...");
//		AndroidDriver ad = (AndroidDriver) this.driver;
//		fixture.wait(1);
//		Point start = getScreenCoordinatesInYAxis(ad,from);
//		Point end = getScreenCoordinatesInYAxis(ad,to);
//		System.out.println(start.x + "..." +start.y+"..."+end.x+"..."+end.y);
//		ad.swipe(start.x,start.y,end.x,end.y,200);
//		fixture.wait(1);
	}


}
