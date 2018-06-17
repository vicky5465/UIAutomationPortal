package scenarios;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import configuration.ConfigData;

import pageobjects.PageObjects;
import utils.driver.DriverFixtures;
import utils.file.JSONManager;
import utils.webservice.WebServiceUtility;

import net.minidev.json.JSONArray;
import utils.webservice.APIManager;


public class CommonScenariosForWeb extends CommonScenarios{
	DriverFixtures fixture;
	APIManager api;
	public CommonScenariosForWeb(WebDriver driver, PageObjects pObj){
		super(driver, pObj);
		fixture = new DriverFixtures(driver);
		api = new APIManager();
	}


	private void disablePopUp() {
		fixture.wait(3);
//		   StringBuilder sb=new StringBuilder("var chat_window = document.getElementById('habla_panel_div');");
		System.out.println("element is: " + fixture.verifyPageContainsElement(driver.findElement(By.id("olark-wrapper"))));
		if(fixture.verifyPageContainsElement(driver.findElement(By.id("olark-wrapper")))) {
		StringBuilder sb=new StringBuilder("var chat_window = document.getElementById('olark-wrapper');");

		 sb.append("chat_window['style'] = 'display: none';");
		   JavascriptExecutor jExecutor=(JavascriptExecutor)driver;
		   jExecutor.executeScript(sb.toString());
		}
		
	}

	

}
