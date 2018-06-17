package pageobjects;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;

public class PageObjects {



		//to get the Email Input Box 
		@FindBy(id="email")//for browser UI
		@AndroidFindBy(id="") //for Android native UI 
		@iOSFindBy(xpath="//XCUIElementTypeCell/XCUIElementTypeTextField")//for iOS native UI
		public WebElement inputboxEmail;

		//to get the Password Input Box on Login Page
		@FindBy(id="password")//for browser UI
		@AndroidFindBy(id="") //for Android native UI 
		@iOSFindBy(xpath="//XCUIElementTypeSecureTextField")//for iOS native UI
		public WebElement inputboxPassword;

		//to get the Log In Button on Login Page
		@FindBy(id="submit")//for browser UI
		@AndroidFindBy(id="") //for Android native UI 
		@iOSFindBy(xpath="//XCUIElementTypeButton")//for iOS native UI
		public WebElement btnLogin;

		//to get the forgot password link on Login Page
		//	@FindBy(id="")//for browser UI
		@AndroidFindBy(id="") //for Android native UI 
		//	@iOSFindBy(xpath="")//for iOS native UI
		public WebElement forgotPwd;


}
