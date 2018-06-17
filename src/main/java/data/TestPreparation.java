package data;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;


import scenarios.CommonScenarios;
import scenarios.CommonScenariosForAndroid;
import scenarios.CommonScenariosForWeb;
import configuration.ConfigData;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import pageobjects.PageObjects;
import utils.driver.AppiumServer;
import utils.driver.DeviceManager;

public class TestPreparation {
	static String port = null;
	static CommonScenarios scenario;


	public static synchronized WebDriver startPreparation(ConfigData configData){
		startAppium(configData);
		WebDriver driver = startDriver(configData);
		return driver;
	}

	//	to get the specific scenarios before starting tests
	public static CommonScenarios getScenarios(ConfigData configData, WebDriver driver, PageObjects pObject) {
		//to get the platform form config
		String platform = configData.getPlatform();
		System.out.println(platform);
		if(platform.equalsIgnoreCase("web")){
			//			scenario = new CommonScenariosForWeb(driver);
			scenario = new CommonScenariosForWeb(driver,pObject);
			System.out.println("this is web");
		}else if (platform.equalsIgnoreCase("ios")){
			//			scenario = new CommonScenarios(driver);
			scenario = new CommonScenarios(driver,pObject);
		}else if (platform.equalsIgnoreCase("android")){
			//			scenario = new CommonScenariosForAndroid(driver);
			scenario = new CommonScenariosForAndroid(driver, pObject);
		}
		return scenario;
	}

	private static void startAppium(ConfigData configData){
		String platform = configData.getPlatform();
		System.out.println("platform: " + platform);


		if (platform.equalsIgnoreCase("WEB")) {
			System.out.println("No need to start appium for web automation");
		}
		else{
			port = AppiumServer.startAppium(configData);
		}

	}

	private static WebDriver startDriver(ConfigData configData){
		System.out.println("Driver is starting now");
		WebDriver driver = null;
		//to get the testing browser from config
		String browser = configData.getBrowser();
		//to get the app name or url from config
		String application = configData.getApplication();
		//to get the platform name: Web, iOS, android, androidWeb, iOSWeb
		String platform = configData.getPlatform();
		String deviceID = configData.getUDID();
		String deviceName = configData.getDevice();
		//to get the device platform version
		String devicePlatformVersion = configData.getDeviceVersion();
		//to get appium url
		String url = "http://127.0.0.1:" + port + "/wd/hub";
		//		String url = "http://0.0.0.0:" + port + "/wd/hub";
		//to check whether the device is in use
		while (DeviceManager.isDeviceInUse(deviceID)) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		try {
			//set capability
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("deviceName", deviceName);
			capabilities.setCapability("udid", deviceID);
			capabilities.setCapability("platformName", platform);
			capabilities.setCapability("platformVersion", devicePlatformVersion);

			if (browser != null) {
				capabilities.setCapability("browserName", browser);
			}
			File app = new File(application);
			if (app.exists()) {
				capabilities.setCapability("app", app.getAbsolutePath());
				capabilities.setCapability("browserName", "");
				browser = null;
			}
			//chrome browser on android devices
			if(platform.equalsIgnoreCase("AndroidWeb"))
			{
				capabilities = DesiredCapabilities.android();
				//desiredCapabilities.setCapability("device", "Android");
				//vicky chanegd the app capability to NULL
				capabilities.setCapability("app", "");
				capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
				capabilities.setCapability("platformName", "Android");
				//				capabilities.setCapability("platformVersion", "6.0.1");
				capabilities.setCapability("platformVersion", "6.0.1");
				//				capabilities.setCapability("deviceName", "1215fc0d21743d03");
				capabilities.setCapability("deviceName", "0715f73d5849103a");
				capabilities.setCapability("appPackage", "com.android.chrome");
				capabilities.setCapability("appActivity","com.google.android.apps.chrome.Main");
				//  desiredCapabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, -1);
				//desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,2147482);
				driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:"+port+"/wd/hub"), capabilities);

				//need to add scenario here
			}		

			//safari browser on ios devices
			else if(platform.equalsIgnoreCase("iOSWeb"))
			{
				System.out.println("running on safari now");
				String command = "ios_webkit_debug_proxy -c 845d7b22a0c34d7f1e51ebbbb96bca4056ac9d8c:27753 -d";
				System.out.println(command);
				Process p = Runtime.getRuntime().exec(command);

				capabilities = new DesiredCapabilities();
				capabilities.setCapability("deviceName", "iPhone 6s");
				capabilities.setCapability("platformName", "iOS");
				capabilities.setCapability("platformVersion", "9.0.2");
				capabilities.setCapability("browserName", "safari");
				capabilities.setCapability("udid", "845d7b22a0c34d7f1e51ebbbb96bca4056ac9d8c");
				//				capabilities.setCapability("fullReset", true);

				driver = new IOSDriver(new URL("http://127.0.0.1:"+port+"/wd/hub"), capabilities);//instantiate driver

				// 		  driver.manage().timeouts().implicitlyWait( 20,TimeUnit.SECONDS);
				// 		  driver.get("http://www.movoto.com");
			}
			if (platform.equalsIgnoreCase("Android")) {
				//				capabilities.setCapability("automationName", "XCUITEST");
				System.out.println("start setting for capacities " );

				//for appium 1.6.4
				capabilities.setCapability("fullReset", false);
				driver = new AndroidDriver<WebElement>(new URL(url), capabilities);
				System.out.println("android driver is stared");
			} else if (platform.equalsIgnoreCase("iOS")) {
				//auto accept the alerts
				capabilities.setCapability("automationName", "XCUITEST");

				capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, "1.6.4");
				capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
				//				capabilities.setCapability("autoAcceptAlerts", true);


				driver = new IOSDriver<WebElement>(new URL(url), capabilities);
				driver.switchTo().alert().accept();

			} else if (platform.equalsIgnoreCase("Web")) {
				driver = startWebDriver(configData);
			} 

			System.out.println("testing driver is: " + driver);
			//			driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
			DeviceManager.addDevice(deviceID);

			if (browser != null) {
				System.out.println(application);
				driver.get(application);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	private static WebDriver startWebDriver(ConfigData configData) throws MalformedURLException {
		WebDriver driver = null;
		String driverPath = configData.getDriverPath();
		DesiredCapabilities capabilities = null;
		boolean isDocker = false;//test ff and chrome run on docker image, right now only have chrome and ff
		if(isDocker)
			driver = startBrowser(configData.getBrowser());
		else{
			switch (configData.getBrowser()) {
			case "Firefox":
				System.setProperty("webdriver.gecko.driver", driverPath);
				FirefoxOptions ffOptions = new FirefoxOptions();
				//			ffOptions.setBinary("/Applications/Firefox.app/Contents/MacOS/firefox"); 
				capabilities = DesiredCapabilities.firefox();
				//			capabilities.setCapability("moz:firefoxOptions", ffOptions);
				capabilities.setCapability("marionette", true);
				//set more capabilities as per your requirements

				//			FirefoxDriver driver = new FirefoxDriver(capabilities);
				//			File profileDir = new File("profiles/firefox_40");
				//			if (profileDir.exists() && profileDir.isDirectory()) {
				//				FirefoxProfile profile = new FirefoxProfile(profileDir);
				//				driver = new FirefoxDriver(profile);
				//			} else {
				//			System.out.println(capabilities);
				driver = new FirefoxDriver(capabilities);
				//			}
				//			driver.manage().timeouts().implicitlyWait(5L, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				driver.manage().deleteAllCookies();

				break;
			case "Chrome":

				if (driverPath != null) {
					System.setProperty("webdriver.chrome.driver", driverPath);
					System.out.println("driverPath=" + driverPath);

					//Vicky changed the chrome driver path for windows
					//				System.setProperty("webdriver.chrome.driver", "D:\\appautomation\\automationScripts\\drivers\\chromedriver.exe");
					capabilities = DesiredCapabilities.chrome();
					ChromeOptions options = new ChromeOptions();

					// Create object of HashMap Class
					Map<String, Object> prefs = new HashMap<String, Object>();

					// Set the notification setting it will override the default setting
					prefs.put("profile.default_content_setting_values.notifications", 2);
					// Set the experimental option
					options.setExperimentalOption("prefs", prefs);

					options.addArguments("--kiosk");
					//				options.addArguments("start-maximized");
					options.addArguments("--js-flags=--expose-gc");  
					options.addArguments("--enable-precise-memory-info"); 
					options.addArguments("--disable-popup-blocking");
					options.addArguments("--disable-default-apps");
					options.addArguments("--disable-notifications");
					//				options.addArguments("test-type=browser");

					options.addArguments("--disable-extensions");
					//				options.addArguments("test-type");
					options.addArguments("start-fullscreen");
					options.addArguments("--enable-automation");
					options.addArguments("--disable-infobars");
					driver = new ChromeDriver(capabilities);
					driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
					driver.manage().window().maximize();
					driver.manage().deleteAllCookies();
				}
				break;
				//this part only be run under windows
			case "IExplore":
				System.setProperty("webdriver.ie.driver", driverPath);
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
				break;
			case "Safari":
				//TestProperties testProperties = dto.getTestProperties();

				capabilities = DesiredCapabilities.safari();
				SafariOptions safariOptions = new SafariOptions();
				safariOptions.setUseCleanSession(true);
				//			SafariOptions.fromCapabilities(capabilities).setUseCleanSession(true);
				capabilities.setCapability(SafariOptions.CAPABILITY, safariOptions);
				capabilities.setCapability(SafariOptions.CAPABILITY, new SafariOptions()); 
				//			capabilities.setVersion(configData.getDeviceVersion());
				//			capabilities.setVersion(configData.getPlatform().toUpperCase());
				//			options.addArguments("start-fullscreen");
				driver = new SafariDriver(capabilities);
				driver.get("http://agent-ng.movoto.net/");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				//			driver.manage().deleteAllCookies();


				break;
			default:
				break;
			}
		}
		return driver;
	}

	private static WebDriver startBrowser(String browser) throws MalformedURLException {
		WebDriver driver = null;
		String port = "4444";
		DesiredCapabilities capabilities = new DesiredCapabilities();

		if("Firefox".equals(browser)){
			System.out.println("testing on FF!");

			System.setProperty("webdriver.gecko.driver", "/drivers/geckodriver");
			capabilities = DesiredCapabilities.firefox();
			//			capabilities.setCapability("moz:firefoxOptions", ffOptions);
			capabilities.setCapability("marionette", true);
			//set more capabilities as per your requirements

			driver = new RemoteWebDriver(new URL("http://localhost:"+port+"/wd/hub"), capabilities);

			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
		}
		else if("Chrome".equals(browser)){
			System.out.println("testing on chrome!");
			String driverPath = "/drivers/chromedriver";
			System.setProperty("webdriver.chrome.driver", driverPath);
			System.out.println("driverPath=" + driverPath);

			capabilities = DesiredCapabilities.chrome();

			driver = new RemoteWebDriver(new URL("http://localhost:"+port+"/wd/hub"), capabilities);
			driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
		}

		return driver;
	}

}
