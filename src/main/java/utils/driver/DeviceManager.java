package utils.driver;

import java.util.HashSet;
import java.util.Set;

public class DeviceManager {
private static Set<String> deviceList = new HashSet<String>();
	
	public static synchronized boolean addDevice(String device){
		return deviceList.add(device);
	}
	
	public static synchronized boolean isDeviceInUse(String device){
		return deviceList.contains(device);
	}
	
	public static synchronized boolean removeDevice(String device){
		return deviceList.remove(device);
	}
}
