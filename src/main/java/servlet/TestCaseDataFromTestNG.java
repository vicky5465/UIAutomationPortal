package servlet;

import org.openqa.selenium.WebDriver;

import configuration.ConfigData;
import pageobjects.PageObjects;
import scenarios.CommonScenarios;
import data.TestData;

public class TestCaseDataFromTestNG {
	protected ConfigData configData;
	protected TestData testData;
	protected WebDriver driver;
	protected CommonScenarios scenario;
	protected PageObjects pObject;
	
	
	public TestCaseDataFromTestNG() {
	}


	public TestCaseDataFromTestNG(ConfigData configData, TestData testData, WebDriver driver, CommonScenarios scenario,
			PageObjects pObject) {
		super();
		this.configData = configData;
		this.testData = testData;
		this.driver = driver;
		this.scenario = scenario;
		this.pObject = pObject;
	}


	public ConfigData getConfigData() {
		return configData;
	}


	public void setConfigData(ConfigData configData) {
		this.configData = configData;
	}


	public TestData getTestData() {
		return testData;
	}


	public void setTestData(TestData testData) {
		this.testData = testData;
	}


	public WebDriver getDriver() {
		return driver;
	}


	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}


	public CommonScenarios getScenario() {
		return scenario;
	}


	public void setScenario(CommonScenarios scenario) {
		this.scenario = scenario;
	}


	public PageObjects getpObject() {
		return pObject;
	}


	public void setpObject(PageObjects pObject) {
		this.pObject = pObject;
	}
	
	
}
