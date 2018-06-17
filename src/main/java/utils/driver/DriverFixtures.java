package utils.driver;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import io.appium.java_client.AppiumDriver;
//import io.appium.java_client.DeviceActionShortcuts;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class DriverFixtures {
	WebDriver driver;
	public DriverFixtures(WebDriver driver){
		this.driver = driver;
	}
	/**
	 * wrap the webdriver calls
	 */

	//this functions is used to type in the data to input box
	public boolean typeDataInto(WebElement element, String data){
		element.clear();
//		implicitlyWait(1);
		element.sendKeys(data);
		return true;
	}

	//to open testing url
	public void get(String url){
		driver.get(url);
		wait(10);
	}

	//this is used to implicit wait
//	public void implicitlyWait(int time){
//		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
//	}

	//this is used to thread wait
	public void wait(int time){
		try {
			Thread.sleep(time*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void wait(double time){
		try {
			Thread.sleep((long) (time*1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//this is used to wait for a specific element
	public void waitForElement(WebElement element){
		//		//in jenkins, it is hard to pass for just wait for element visible
		//		Reporter.log("start adding implicit wait");
		//		try {
		//			System.out.println(new Date());
		//			Thread.sleep(5000);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		Reporter.log("implicit wait end");


		//		try {
		//			new FluentWait<WebDriver>(driver).withTimeout(30L, TimeUnit.SECONDS)
		//					.pollingEvery(500L, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class)
		//					.until(new ExpectedCondition<Boolean>() {
		//						public Boolean apply(WebDriver d) {
		//							return element.isDisplayed();
		//						}
		//					});
		//		} catch (Exception e) {
		//			// TODO: handle exception
		//		}

		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	//this is used to click the element
	public void click(WebElement element){
		element.click();
	}

	//this is used to click the element and wait for some sec
	public void clickAndWait(WebElement element, int time){
		element.click();
		wait(time);
	}

	//this is used to verify whether the element exist on the page
	public boolean verifyPageContainsElement(WebElement element) {
//				waitForElement(element);
		wait(3);
		//to check whether the element is displayed on the page
		if(element!=null){
			try{
				return element.isDisplayed();
			}catch (NoSuchElementException e) {
				return false;
			}
		}
		return false;
	}

	//this is used to get the text from the element
	public String getText(WebElement element) {
		return element.getText();
	}

	//this is used to scroll the scroll bar to the specific element
	//this is for web using
	public void scrollTo(WebElement element) {
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);",element);
		wait(3);
	}

	public List<String> getTexts(List<WebElement> elements) {
		List<String> texts = new ArrayList<String>();
		for(WebElement element: elements){
			String text = getText(element);
			texts.add(text);
		}
		return texts;
	}

	//to hide the soft keyboard for Android
	@SuppressWarnings("unchecked")
	public void hideKeyboard(WebDriver driver){
		try{
			((AppiumDriver<WebElement>) driver).hideKeyboard();
		}catch(Exception e){

		}

	}

	//to quit the driver
	public void quit(String deviceID) {
		driver.quit();
		try {
			if (DeviceManager.isDeviceInUse(deviceID)) {
				DeviceManager.removeDevice(deviceID);
			}
		} catch (Exception e) {
			System.out.println("the testing device can't be removed");
		}
	}

	//to get the device's screen width
	public int getWindowWidth(WebDriver driver){
		return driver.manage().window().getSize().width;
	}

	//to get the device's screen height
	public int getWindowHeight(WebDriver driver){
		return driver.manage().window().getSize().height;
	}

	//to swipe from the one point to another point of the screen
	//	need to check whether android driver can use this wipe function?
	@SuppressWarnings("unchecked")
	public void swipeTo(int startx, int starty, int endx, int endy) {
		TouchAction action = new TouchAction((AppiumDriver<WebElement>) driver);
		int dx = endx - startx;
		int dy = endy - starty;
		action.longPress(startx, starty).moveTo(dx, dy).release().perform();
	}

	public void androidSwipeTo(int startx, int starty, int endx, int endy){
		AndroidDriver ad = (AndroidDriver) driver;
//		ad.swipe(startx, starty, endx, endy, 200);
	}

//	@SuppressWarnings("unchecked")
//	public void scroll(int startx, int starty, int endx, int endy) {
//		AndroidDriver driver = (AndroidDriver) this.driver;
//
//	}

	public void clearCookies(){
		driver.manage().deleteAllCookies();
		wait(2);
	}

	public void threadWait(int seconds){
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param selectElement
	 * @param optionValue
	 */
	public void selectByValue(WebElement selectElement, String optionValue) {
		Select dropdown = new Select(selectElement);
		dropdown.selectByValue(optionValue);
	}

	/**
	 * 
	 * @param selectElement
	 * @param optionIndex: the index of the selection option
	 */
	public void selectByIndex(WebElement selectElement, int optionIndex) {
		Select dropdown = new Select(selectElement);
		dropdown.selectByIndex(optionIndex);
	}

	public boolean switchToWindow() {
		System.out.println("test");
		String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
//		System.out.println(parentWindowHandler);
		String subWindowHandler = null;

		Set<String> handles = driver.getWindowHandles();// get all window handles
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext()){
			subWindowHandler = iterator.next();
//			System.out.println(subWindowHandler);
		}
		driver.switchTo().window(subWindowHandler); // switch to popup window
		// perform operations on popup
		return true;
	}


}
