package utils.file;

import java.io.File;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.math.BigDecimal;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;


public class ExcelFileManager {

	public int counts = 0;
	private String sheetName;
	private Sheet sheet = null;
	private String readOrWrite;
	private String locationPath;
	public String comments = "";
	private FileInputStream fin;
	private FileOutputStream out;
	private boolean isNew = false;
	private HSSFWorkbook workbook;
	private XSSFWorkbook workbookXssf;
	private ArrayList<String> arrayList;
	private boolean isErrorFound = false;

	private static Set<String> lockedFiles = new HashSet<>();

	public ExcelFileManager() {
		arrayList = new ArrayList<String>();
	}

	/**
	 * Description : Verify if the sheet exists in the excel file
	 * 
	 * @return sheetFound
	 */
	private boolean verifyIfSheetExists() {

		boolean sheetFound = false;
		if (workbook != null) {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				String name = workbook.getSheetName(i);
				if (sheetName.equals(name)) {
					return true;
				}
			}
		} else if (workbookXssf != null) {
			for (int i = 0; i < workbookXssf.getNumberOfSheets(); i++) {
				String name = workbookXssf.getSheetName(i);
				if (sheetName.equals(name)) {
					return true;
				}
			}
		}
		return sheetFound;
	}

	/**
	 * Description : Converts and returns string, formula, boolean and numeric
	 * cells as String
	 * 
	 * @return cellValue
	 * @throws UnsupportedEncodingException
	 */
	private String returnStringTypeof(Cell cellType) throws UnsupportedEncodingException {

		String cellValue = "";
		if (cellType != null) {
			switch (cellType.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				cellValue = cellType.toString().trim();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				cellValue = convertNumericCelltoString(cellType).toString().trim();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				boolean cellVal = cellType.getBooleanCellValue();
				cellValue = new Boolean(cellVal).toString().trim();
				break;
			case Cell.CELL_TYPE_FORMULA:
				FormulaEvaluator evaluator = null;
				if (workbook != null)
					evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				else
					evaluator = workbookXssf.getCreationHelper().createFormulaEvaluator();
				cellValue = evaluator.evaluate(cellType).formatAsString();
				if (cellValue.endsWith(".0") || cellValue.endsWith(".00"))
					cellValue = cellValue.substring(0, cellValue.indexOf("."));
				break;
			default:
			}
		}
		return new String(cellValue.trim().getBytes(), "UTF-8");
	}

	/**
	 * Description : Read a cell value from the excel sheet and if the cell
	 * value is numeric then convert it to String
	 * 
	 * @param columnValue
	 * @return strValue
	 */
	private String convertNumericCelltoString(Cell columnValue) {

		String strValue = "";
		if (columnValue != null) {
			double cellVal = columnValue.getNumericCellValue();
			BigDecimal big = new BigDecimal(cellVal);
			strValue = String.valueOf(big);
			if (strValue.contains(".")) {
				double val = Double.valueOf(strValue).doubleValue();
				strValue = String.valueOf(val);
				if (strValue.endsWith("0"))
					strValue = strValue.substring(0, strValue.length() - 1);
			}
		}
		return strValue;
	}

	/**
	 * Description : Loads the Excel sheet
	 * 
	 * @param sheetName
	 * @param locationPath
	 * @param readOrWrite
	 * @throws Exception
	 */
	public void loadExcelSheet(String sheetName, String locationPath, String readOrWrite) throws Exception {

		boolean sheetFound = false;
		this.sheetName = sheetName;
		this.readOrWrite = readOrWrite;
		this.locationPath = locationPath;
		String type = "read";

		if (locationPath.endsWith(".xls") || locationPath.endsWith(".xlsx")) {
			if (readOrWrite.equalsIgnoreCase("read") || readOrWrite.equalsIgnoreCase("write")) {

				if (readOrWrite.equalsIgnoreCase("write"))
					type = "write";
				try {
					openExcelFile(locationPath, type);
					if (locationPath.endsWith(".xls"))
						workbook = new HSSFWorkbook(fin);
					else
						workbookXssf = new XSSFWorkbook(fin);
				} catch (IOException e) {
					e.printStackTrace();
				}
				sheetFound = verifyIfSheetExists();
				if (!sheetFound) {
					if (type.equals("read"))
						;
					else {
						if (workbook != null)
							sheet = workbook.createSheet(sheetName);
						else
							sheet = workbookXssf.createSheet(sheetName);
					}
				} else {
					if (workbook != null)
						sheet = workbook.getSheet(sheetName);
					else
						sheet = workbookXssf.getSheet(sheetName);
					counts = sheet.getPhysicalNumberOfRows();
				}
			} else {
				if (type.equals("write")) {
					isNew = true;
					if (locationPath.endsWith(".xls")) {
						workbook = new HSSFWorkbook();
						sheet = workbook.createSheet(sheetName);
					} else if (locationPath.endsWith(".xlsx")) {
						workbookXssf = new XSSFWorkbook();
						sheet = workbookXssf.createSheet(sheetName);
					}
				}

			}
		}

	}

	private void openExcelFile(String filePath, String mode) throws Exception {
		File file = new File(filePath);
		int counter = 0;
		if (file.exists()) {
			synchronized (lockedFiles) {
				while (isLocked(filePath)) {
					System.out.println("Locked: " + locationPath);
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO: handle exception
					}
					counter++;
					if(counter>=10){
						throw new Exception("Could not open the file: "+filePath);
					}
				}
				this.fin = new FileInputStream(new File(filePath));
				this.locationPath = filePath;
				if (mode.equalsIgnoreCase("write")) {
					lockFile(filePath);
					System.out.println("Locked: " + filePath);
				}
			}
		} else {
			this.fin = null;
		}
	}

	/**
	 * Description : Retrieves all the column data from the given row number and
	 * stores them as header names
	 * 
	 * @param rowIndex
	 * @return TreeMap<String, Integer>
	 */
	private TreeMap<String, Integer> getHeaderNames(int rowIndex) {

		int headerNo = 0;
		int blankHeader = 0;
		String headerName = "";
		Row row = sheet.getRow(rowIndex);
		int lastCell = row.getLastCellNum();
		TreeMap<String, Integer> headers = new TreeMap<String, Integer>();

		for (int j = 0; j < lastCell; j++) {
			Cell cell = row.getCell(j);
			if (cell != null)
				try {
					headerName = returnStringTypeof(cell);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else {
				blankHeader = blankHeader + 1;
				headerName = "MissingHeader" + String.valueOf(blankHeader);
			}
			headerNo = headerNo + 1;
			headers.put(headerName, headerNo);
		}
		return headers;
	}

	/**
	 * Description : Retrieves the data from the column in a row by it's name
	 * 
	 * @param rowIndex
	 * @param columnName
	 * @return resultString
	 */
	public String retrieveRowColumnDataByName(int rowIndex, String columnName) {

		String resultString = "";
		Row row = sheet.getRow(rowIndex);
		int lastRow = sheet.getLastRowNum();
		TreeMap<String, Integer> headers = getHeaderNames(0);

		if (rowIndex <= lastRow) {
			try {
				int lastCell = row.getLastCellNum();
				if (headers.containsKey(columnName)) {
					int columnNo = 1;
					int columnHeader = headers.get(columnName);
					for (int i = 0; i < lastCell; i++) {
						Cell cell = row.getCell(i);
						if (columnNo == columnHeader) {
							if (cell != null)
								resultString = returnStringTypeof(cell);
							else
								resultString = "";
							break;
						}
						columnNo = columnNo + 1;
					}
				} else {
					isErrorFound = true;
					comments = "Column name doesn't exist";
				}
			} catch (Exception e) {
				isErrorFound = true;
				comments = "Row : " + (rowIndex) + " is a blank row";
			}
		} else {
			isErrorFound = true;
			comments = "Row doesn't exist";
		}
		return resultString;
	}

	/**
	 * Description : Retrieves the data from the column in a row by it's number
	 * 
	 * @param rowIndex
	 * @param columnIndx
	 * @return resultString
	 */
	private String retrieveRowColumnData(int rowIndex, int columnIndx) {

		String resultString = "";
		Row row = sheet.getRow(rowIndex);
		int lastRow = sheet.getLastRowNum();
		int columnWidth = sheet.getDefaultColumnWidth();
		if (rowIndex <= lastRow) {
			try {
				int columnNo = 0;
				int lastCell = row.getLastCellNum();
				if (columnIndx <= lastCell - 1) {
					for (int i = 0; i < lastCell; i++) {
						Cell cell = row.getCell(i);
						if (columnNo == columnIndx) {
							if (cell != null)
								resultString = returnStringTypeof(cell);
							else
								resultString = "";
							break;
						}
						columnNo = columnNo + 1;
					}
				} else {
					if (columnNo <= columnWidth)
						resultString = "";
					else {
						isErrorFound = true;
						comments = "Column doesn't exist";
					}
				}
			} catch (Exception e) {
				isErrorFound = true;
				comments = "Row : " + (rowIndex + 1) + " is a blank row";
			}
		} else {
			isErrorFound = true;
			comments = "Row doesn't exist";
		}
		return resultString;
	}

	private String readExcelRowDataByColumnName(int rowIndex, String columnIndex) {

		if (this.readOrWrite == "write") {

		}

		String resultStr = "";
		if (rowIndex <= 0) {
			isErrorFound = true;
			if (rowIndex <= 0)
				comments = "Specify row number greater than 0";
		}
		if (columnIndex.equals("")) {
			isErrorFound = true;
			comments = "Specify column name to retrieve data";
		}
		if (!isErrorFound) {
			try {
				if (readOrWrite.equalsIgnoreCase("read"))
					resultStr = retrieveRowColumnDataByName(rowIndex, columnIndex);
				else
					;
			} catch (Exception e) {

			}
		}
		return resultStr;
	}

	private String readExcelRowDataByColumnNumber(int rowIndex, int columnIndex) {

		if (this.readOrWrite == "write") {

		}

		String resultStr = "";
		if (rowIndex <= 0 || columnIndex <= 0) {
			isErrorFound = true;
			if (rowIndex <= 0 && columnIndex <= 0)
				comments = "Specify both row and column number greater than 0";
			else if (rowIndex <= 0)
				comments = "Specify row number greater than 0";
			else if (columnIndex <= 0)
				comments = "Specify column number greater than 0";
		} else {
			rowIndex = rowIndex - 1;
			columnIndex = columnIndex - 1;
		}
		if (!isErrorFound) {
			try {
				if (readOrWrite.equalsIgnoreCase("read"))
					resultStr = retrieveRowColumnData(rowIndex, columnIndex);
				else
					;
			} catch (Exception e) {

			}
		}
		return resultStr;
	}

	public int getRowCounts() {

		return counts;
	}

	/**
	 * Description : Retrieves the required cell value from the excel sheet with
	 * the specified row and column numbers
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 * @return stringCellValue
	 */
	public String readExcelSheet(String rowNumber, String columnNumber) {

		if (this.readOrWrite == "write") {

		}

		comments = "";
		isErrorFound = false;
		String stringCellValue = "";
		if (rowNumber.contains("."))
			rowNumber = rowNumber.substring(0, rowNumber.indexOf("."));
		try {
			int rowValue = Integer.parseInt(rowNumber);
			try {
				if (columnNumber.contains("."))
					columnNumber = columnNumber.substring(0, columnNumber.indexOf("."));
				int columnVal = Integer.parseInt(columnNumber);
				stringCellValue = readExcelRowDataByColumnNumber(rowValue, columnVal);
			} catch (Exception e) {
				stringCellValue = readExcelRowDataByColumnName(rowValue, columnNumber);
			}
		} catch (Exception e) {

		}
		if (isErrorFound)
			;
		// if(stringCellValue.equals(""))stringCellValue = " ";
		return stringCellValue;
	}

	/**
	 * Description :Removes the loaded Excel sheet.
	 * 
	 */
	public void unloadExcelSheet() {

		try {
			if (readOrWrite.equalsIgnoreCase("write")) {
				writeDataIntoExcel();
			}
			if (fin != null) {
				fin.close();
				fin = null;
			}
			if (isLocked(locationPath)) {
				unLockFile(locationPath);
				System.out.println("Unlocked: " + locationPath);
			}
			workbook = null;
			workbookXssf = null;
			sheet = null;
			arrayList = null;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description :Retrieves the column index of a row by it's name.
	 * 
	 * @param rowColumnValue
	 * @return columnNo
	 * 
	 */
	private int getColumnIndexByName(String rowColumnValue) {

		int columnNo = 0;
		TreeMap<String, Integer> headers = getHeaderNames(0);
		if (headers.containsKey(rowColumnValue)) {
			columnNo = headers.get(rowColumnValue) - 1;
			return columnNo;
		} else {
			isErrorFound = true;
			comments = "Column name doesn't exist";
		}
		return columnNo;
	}

	private void updateExcelSheet(int rowValue, int columnValue, String data)
			throws FileNotFoundException, IOException {

		Row row = null;
		Cell cell = null;
		boolean sheetExists = verifyIfSheetExists();
		if (sheetExists) {
			if (!isNew) {
				if (workbook != null) {
					workbook = new HSSFWorkbook(new FileInputStream(locationPath));
					sheet = workbook.getSheet(sheetName);
				} else {
					workbookXssf = new XSSFWorkbook(new FileInputStream(locationPath));
					sheet = workbookXssf.getSheet(sheetName);
				}
			} else
				isNew = false;
			if (sheet.getRow(rowValue) == null) {
				row = sheet.createRow(rowValue);
				cell = row.createCell(columnValue);
			} else {
				if (sheet.getRow(rowValue).getCell(columnValue) == null)
					cell = sheet.getRow(rowValue).createCell(columnValue);
				else
					cell = sheet.getRow(rowValue).getCell(columnValue);
			}
			cell.setCellValue(data);
			try {
				out = new FileOutputStream(locationPath);
				if (workbook != null)
					workbook.write(out);
				else
					workbookXssf.write(out);
				out.close();
			} catch (Exception e) {

			}
		} else {

		}
	}

	private void writeDataIntoExcel() {

		comments = "";
		isErrorFound = false;
		if (arrayList != null) {
			for (int i = 0; i < arrayList.size(); i++) {
				String rowNum = "";
				String data = arrayList.get(i);
				int rowValue = 0, columnValue = 0;
				String[] rowColumnValue = data.split("%%");
				if (rowColumnValue[0].contains("."))
					rowNum = rowColumnValue[0].substring(0, rowColumnValue[0].indexOf("."));
				else
					rowNum = rowColumnValue[0];
				try {
					rowValue = Integer.parseInt(rowNum);
					try {
						columnValue = (int) Double.parseDouble(rowColumnValue[1]);
						if (rowValue <= 0 || columnValue <= 0) {
							isErrorFound = true;
							if (rowValue <= 0 && columnValue <= 0)
								comments = "Specify both row and column number greater than 0";
							else if (rowValue <= 0)
								comments = "Specify row number greater than 0";
							else if (columnValue <= 0)
								comments = "Specify column number greater than 0";
						} else {
							rowValue = rowValue - 1;
							columnValue = columnValue - 1;
						}
					} catch (Exception e) {
						if (rowValue <= 0) {
							isErrorFound = true;
							comments = "Specify row number greater than 0";
						} else
							rowValue = rowValue - 1;
						if (!isErrorFound)
							columnValue = getColumnIndexByName(rowColumnValue[1]);
					}
				} catch (Exception e) {

				}
				String inputData = rowColumnValue[2];
				if (isErrorFound)
					;
				else {
					try {
						updateExcelSheet(rowValue, columnValue, inputData);
					} catch (FileNotFoundException e) {

					} catch (IOException e) {

					}
				}
			}
		}
	}

	/**
	 * Description :Writes the data into excel sheet.
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 * 
	 * 
	 */
	public void writeIntoExcelSheet(String value, String rowNumber, String columnNumber) {

		if (readOrWrite.equalsIgnoreCase("write")) {
			String rowColumnValue = collateParam(new String[] { rowNumber, columnNumber, value });
			if (!arrayList.contains(rowColumnValue))
				arrayList.add(rowColumnValue);
		} else {

		}
	}

	private String collateParam(String[] params) {

		StringBuilder param = new StringBuilder();
		for (String temp : params) {
			if (params.length > 1 && param.length() > 0)
				param.append("%%");
			param.append(temp);
		}
		return param.toString();
	}

	public int findRowNumberForKey(String key) throws UnsupportedEncodingException {
		int rowNum = -1;
		if (key != null) {
			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				System.out.println("Row: " + i + "\t");
				for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);
					String cellValue = returnStringTypeof(cell);
					System.out.print(cellValue + "\t");
					if (cellValue != null && cellValue.equals(key)) {
						rowNum = i;
						break;
					}

				}
				if (rowNum != -1) {
					break;
				}
			}
		}
		System.out.println("Row number: " + rowNum);
		return rowNum + 1;
	}

	public void deleteRowForKey(String key) {
		int rowNum = -1;
		try {
			rowNum = findRowNumberForKey(key);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rowNum >= 0) {
			Row row = sheet.getRow(rowNum);
			sheet.removeRow(row);
			int lastRowNum = sheet.getLastRowNum();
			if (rowNum >= 0 && rowNum < lastRowNum) {
				sheet.shiftRows(rowNum + 1, lastRowNum, -1);
			}
		}
	}

	private static synchronized boolean isLocked(String file) {
		return lockedFiles.contains(file);
	}

	private static synchronized void lockFile(String file) {
		lockedFiles.add(file);
	}

	private static synchronized boolean unLockFile(String file) {
		return lockedFiles.remove(file);
	}
	
	
	public static void main(String[] args){
		ExcelFileManager efm = new ExcelFileManager();
		efm.retrieveRowColumnDataByName(2, "UserName");
	}
}