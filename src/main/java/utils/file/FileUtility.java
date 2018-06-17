package utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.testng.Reporter;

//import report.ReportManager;



//import org.apache.commons.io.FilenameUtils;

public class FileUtility {
	private ThreadLocal<ExcelFileManager> localExcel = new ThreadLocal<ExcelFileManager>();
	
	public static Map<String, String> readPropertiesFile(String filePath) throws IOException {
		File configFile = new File(filePath);
		Map<String, String> map = new HashMap<String, String>();
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			reader.close();
			for (final String name : props.stringPropertyNames()) {
				if (props.getProperty(name) != null && props.getProperty(name).length() > 0) {
					map.put(name, props.getProperty(name));
				} else {
					map.put(name, null);
				}
			}
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		}
		return map;
	}

//	public static Map<String, String> readFileAsMap(String filePath) throws IOException {
//		Map<String, String> map = new HashMap<String, String>();
//		if (filePath != null) {
//			String extension = FilenameUtils.getExtension(filePath);
//			switch (extension) {
//			case FileType.FILE_TYPE_PROPERTIES:
//				map = readPropertiesFile(filePath);
//				break;
//			case FileType.FILE_TYPE_JSON:
//				
//				break;
//			case FileType.FILE_TYPE_EXCEL:
//
//				break;
//			case FileType.FILE_TYPE_EXCEL_OLD:
//
//				break;
//			default:
//				break;
//			}
//		}
//		return map;
//	}
	
	public static Map<String, String> readJsonFile(String filePath) throws IOException {
		File configFile = new File(filePath);
		Map<String, String> map = new HashMap<String, String>();
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			reader.close();
			for (final String name : props.stringPropertyNames()) {
				if (props.getProperty(name) != null && props.getProperty(name).length() > 0) {
					map.put(name, props.getProperty(name));
				} else {
					map.put(name, null);
				}
			}
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		}
		return map;
	}
	
	public void openExcelSheet(String path, String sheet, String mode) {
		ExcelFileManager excel = new ExcelFileManager();
		try {
			excel.loadExcelSheet(sheet, path, mode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		localExcel.set(excel);
	}

	public String getFromExcelRowAndColumn(int row, String header) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			return excel.retrieveRowColumnDataByName(row, header);
		}
		return null;
	}

	public void closeExcelSheet(String path, String sheet, String mode) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			excel.unloadExcelSheet();
		}

	}

	public void writeToExcel(String data, int row, int column) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			excel.writeIntoExcelSheet(data, String.valueOf(row), String.valueOf(column));
		}
	}

	public int getExcelRowNumberForKey(String key) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			try {
				return excel.findRowNumberForKey(key);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return -1;
	}

	public int getExcelRowCount() {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			return excel.getRowCounts();
		}
		return 0;
	}


	public boolean deleteRowForKey(String key) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			excel.deleteRowForKey(key);
		}
		return true;
	}


	public Object getValueFromJson(String jsonPath, String json) {
		try {
			return JSONManager.getValueFromJson(jsonPath, json);
		} catch (Exception e) {
			return null;
		}
		
	}


	public boolean connectToDatabase(String db) {
		// TODO Auto-generated method stub
		return false;
	}


	public Object executeQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean closeDatabaseConnection() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean wait(int seconds) {
		try {
			long millis = seconds * 1000;
			Thread.sleep(millis);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	
	public void setCurrentTestMethod(String name) {
//		Reporter.getCurrentTestResult().setMethod(name);
	}

	
	
	
	public String getCurrentDate() {
		return new Date().toString();
	}

	
}