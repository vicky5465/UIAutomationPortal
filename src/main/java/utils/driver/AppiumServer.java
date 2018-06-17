package utils.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.SocketException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;

import configuration.ConfigData;


//import com.movoto.data.TestDTO;

public class AppiumServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * start appium with auto generated ports : appium port, chrome port, and bootstap port
	 * return: port, in order to pass this port to drivers
	 */
	public static String startAppium(ConfigData configData){
//	{
//		// start appium server
		String port = getPort();
		System.out.println("port is: " + port);
//		String chromePort = ap.getPort();
//		String bootstrapPort = ap.getPort();
//		
		String params = " --full-reset --session-override --log-level info";
//		String params = " --no-reset --session-override --log-level info";//change to not reset the app, need to move to config
		//not sure what this is used for, comment out first for now
		if(configData.getPlatformType().equalsIgnoreCase("ios_safari")){
			//for ios safari browser, no need to run full reset
			params = " --no-reset --session-override";
		}
		
		/*
		 * need to test this portion
		 */
//		if(!configData.isFullReset()){
//			params = " --no-reset --session-override";
//		}
		
//		
//		//changed by Vicky on 7/6/2016
		 
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("os is: " + os);
		if(os.contains("windows")){
			startAppiumOnWindows(port, params, os);
		}
		else{		
			//String command = "appium --session-override -p "+port+" --chromedriver-port "+chromePort+" -bp "+bootstrapPort;
//			String command = "node /Applications/Appium.app/Contents/Resources/node_modules/appium/build/lib/main.js --address 127.0.0.1 --port "+port+params;
//			String command = "node /Users/vicky/local/lib/node_modules/appium/build/lib/main.js --address 127.0.0.1 --port "+port+params;
			String command = "node /usr/local/lib/node_modules/appium/build/lib/main.js --address 0.0.0.0 --port "+port+params;
//			String command = "node /Users/vicky/local/lib/node_modules/appium/build/lib/main.js -a 0.0.0.0 -p "
//					+port + " --default-capabilities "+ "'{"+ "'fullReset':true}'";
			System.out.println(command);
			String output = 
					startAppiumOnMac(command);

			System.out.println(output);

			if(output.contains("not"))
			{
				System.setProperty("PATH", "");
				System.out.println("\nAppium is not installed");
				System.exit(0);
			}
		}
		return port;
	}

	//written by vicky for runing appium server on Windows
	public static void startAppiumOnWindows(String port, String params, String os) {
		String nodeCommand = "";
		String appiumCommand ="";
		if(os.contains("windows")){
			nodeCommand = "D:/Appium/node";
			appiumCommand = "D:/Appium/node_modules/appium/bin/appium.js";
		}else {
			nodeCommand = "node /Applications/Appium.app/Contents/Resources/node_modules/appium/build/lib/main.js";
		}

		CommandLine command = new CommandLine(nodeCommand);
//		command.addArgument(appiumCommand,false);
		
//		String command = "node /Applications/Appium.app/Contents/Resources/node_modules/appium/build/lib/main.js --address 127.0.0.1 --port "+port+params;
		
		command.addArgument("--address", false);
		command.addArgument("127.0.0.1");
		command.addArgument("--port", false);
		command.addArgument(port);
		command.addArgument("--full-reset", false);
		command.addArgument("--session-override", false);
		//Apache Commons Exec:
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(command, resultHandler);
			Thread.sleep(2000);
			System.out.println("Appium server started.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	//to get available port for appium
	private static String getPort()
	{
		String port = null;
		ServerSocket socket;
		try {
			socket = new ServerSocket(0);
			socket.setReuseAddress(true);
			port = Integer.toString(socket.getLocalPort()); 
			socket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}		
		return port;
	}
	
	public static String startAppiumOnMac(String command)
	{
		
//		CommandLine command = new CommandLine(
//				"/Applications/Appium.app/Contents/Resources/node/bin/node");
//		command.addArgument(
//				"/Applications/Appium.app/Contents/Resources/node_modules/appium/build/lib/main.js",
//				false);
//		command.addArgument("--address", false);
//		command.addArgument("127.0.0.1");
//		command.addArgument("--port", false);
//		command.addArgument(port);
//		command.addArgument("--full-reset", false);
//		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
//		DefaultExecutor executor = new DefaultExecutor();
//		executor.setExitValue(1);
//		try {
//			executor.execute(command, resultHandler);
//			Thread.sleep(5000);
//			System.out.println("Appium server started.");
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
		Process p;
		String allLine="";

		try {
			p = Runtime.getRuntime().exec(command);
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line="";			
			while((line=r.readLine()) != null){
				allLine=allLine+""+line+"\n";
				if(line.contains("interface listener"))
					break;
				System.out.println(line);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allLine;
	}
}
