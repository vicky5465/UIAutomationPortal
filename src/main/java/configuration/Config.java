package configuration;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private final String APPIUM_PATH = "APPIUM_PATH";
	private final String NODE_WIN_PATH = "NODE_PATH_WINDOWS";
	private final String APPIUM_WIN_PATH = "APPIUM_PATH_WINDOWS";
	private final String IOS_DOWNLOAD_URL = "IOS_DOWNLOAD_URL";
	private final String ANDORID_DOWNLOAD_URL = "ANDORID_DOWNLOAD_URL";
	private final String IOS_DOWNLOADED_PATH = "IOS_DOWNLOADED_PATH";
	private final String ANDORID_DOWNLOADED_PATH = "ANDORID_DOWNLOADED_PATH";
	private final String IOS_DES_PATH = "IOS_DES_PATH";
	private final String ANDORID_DES_PATH = "ANDORID_DES_PATH";
	
	
	public String getPathConfig(String key) {
		Properties props = new Properties();
		String dir = System.getProperty("user.dir")+"/config/config.properties";
//		System.out.println(dir);
//		System.out.println(Config.class.getClassLoader().getResourceAsStream("config/config.properties"));
		try {
//			props.load(Config.class.getClassLoader().getResourceAsStream("config/config.properties"));
			props.load(new FileReader(dir));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return props.getProperty(key);
	}


	public String getAppiumPath() {
		return getPathConfig(APPIUM_PATH);
	}


	public String getNodeWinPath() {
		return getPathConfig(NODE_WIN_PATH);
	}



	public String getAppiumWinPath() {
		return getPathConfig(APPIUM_WIN_PATH);
	}

	
	public String getIosDownloadUrl() {
		return getPathConfig(IOS_DOWNLOAD_URL);
	}

	

	public String getAndroidDownloadUrl() {
		return getPathConfig(ANDORID_DOWNLOAD_URL);
	}

	
	public String getIosDownloadedPath() {
		return getPathConfig(IOS_DOWNLOADED_PATH);
	}

	

	public String getAndroidDownloadedPath() {
		return getPathConfig(ANDORID_DOWNLOADED_PATH);
	}


	public String getIosDestPath() {
		return getPathConfig(IOS_DES_PATH);
	}

	

	public String getAndroidDesPath() {
		return getPathConfig(ANDORID_DES_PATH);
	}

	
}
