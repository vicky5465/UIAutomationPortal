package configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import utils.file.FileUtility;


public class ConfigData {
	
	Map<String, String> cd = new HashMap<String, String>();
	
	public ConfigData(String testName){
		cd = populateConfigData(testName);
	}

	//get the data from config
		public synchronized Map<String, String> populateConfigData(String testName) {
			System.out.println(testName);
			Map<String, String> propMap = new HashMap<String, String>();
			String userDir = System.getProperty("user.dir");
			System.out.println("User Dir: " + userDir);
			//to get the platform name: web, android, ios
			String platform = testName.split("_")[1].toLowerCase();
			//path of the config file
			String testConfigFilePath = userDir + "/config/" + platform+ "." + "properties";
			System.out.println(testConfigFilePath);
			
			//trying to fix for Web
//			String file = platform+ ".properties";
//			File myFile = new File(userDir,file);
			
			try {
//				String file = platform+ ".properties"; //"/config/" + platform+ "." + "properties";
//				InputStream fileStream = this.getClass().getClassLoader().getResourceAsStream(file);
//				String filePath = IOUtils.toString(fileStream);
//					System.out.println("file path" + filePath);
			
			
				//store all the key value pair of config to Map
				propMap = FileUtility.readPropertiesFile(testConfigFilePath);
//				propMap = FileUtility.readPropertiesFile(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return propMap;
		}

	//remove the ORPath
//	public String getORPath(){
//		return cd.get("OBJECT_REPOSITORY_FILE_PATH");
//	}
	
	public String getPlatform(){
		return cd.get("PLATFORM_NAME");
	}
	
	public String getApplication(){
		return cd.get("APPLICATION");
	}
	
	public String getDevice(){
		return cd.get("DEVICE_NAME");
	}
	
	public String getUDID(){
		return cd.get("UDID");
	}
	
	public String getPlatformType(){
		return cd.get("PLATFORM_TYPE");
	}
	
	public String getDeviceVersion(){
		return cd.get("DEVICE_VERSION");
	}

	
	private String getStepScreenShotEnabled(){
		return cd.get("STEP_SCREENSHOT_ENABLED");
	}
	
	private String getAssertScreenShotEnabled(){
		return cd.get("ASSERT_SCREENSHOT_ENABLED");
	}
	
	public boolean isStepScreenShotEnabled(){
		if (getStepScreenShotEnabled().equals("false"))
			return false;
		else
			return true;
	}
	
	public boolean isAssertScreenShotEnabled(){
		if (getAssertScreenShotEnabled().equals("false"))
			return false;
		else
			return true;
	}
	
	public String getDriverPath(){
		return cd.get("DRIVER_PATH");
	}
	
	public String getBrowser(){
		return cd.get("BROWSER_NAME");
	}
	
	public boolean isFullReset(){
		if (cd.get("FULL_RESET").equals(null)||cd.get("FULL_RESET").equalsIgnoreCase("true"))
			return true;
		return false;
	}
	
	public String getSFMovotoFullURL(){
		return cd.get("SF_MOVOTOFULL");
	}
	
	public String getSFProductionURL(){
		return cd.get("SF_PRODUCTION");
	}
	
	public String getBaseURL(){
		return cd.get("BASE_URL");
	}
	
	//remove this function, directly add in the code
//	public boolean disableFullReset(){
//		System.out.println(cd.get("DISABLE_FULL_RESET"));
//		if (cd.get("DISABLE_FULL_RESET").equals(null)||cd.get("DISABLE_FULL_RESET").equals("false"))
//			return false;
//		else
//			return true;	
//		
//	}

	//removed from the config
//	public boolean isAutoAcceptAlertEnabled(){
//		if (cd.get("AUTO_ACCEPT_ALERT_ENABLED").equals("false"))
//			return false;
//		else
//			return true;		
//	}
}
