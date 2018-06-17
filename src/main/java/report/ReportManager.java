package report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;



import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minidev.json.JSONObject;

public class ReportManager {
//	private static ThreadLocal<TestResult> m_currentTestResult = new InheritableThreadLocal<>();
//	private static Map<String, TestResult> map = new HashMap<>();
//	private static ThreadLocal<Map<String, String>> instruction = new InheritableThreadLocal<>();
//	private static Map<TestResult, List<Integer>> m_methodOutputMap = Maps.newHashMap();
//
//	/**
//	 * All output logged in a sequential order.
//	 */
//	private static List<String> m_output = new Vector<>();
//
//	public static void setCurrentTestResult(TestResult m) {
//		map.put(m.getName(), m);
//		m_currentTestResult.set(m);
//		Map<String, String> instructionMap = new HashMap<>();
//		instruction.set(instructionMap);
//	}
//
//	public static TestResult getTestResult(String name) {
//		TestResult res = map.get(name);
//		return res;
//	}
//
//	/**
//	 * @return the current test result.
//	 */
//	public static TestResult getCurrentTestResult() {
//		return m_currentTestResult.get();
//	}
//
//	public static List<String> getOutput(String name) {
//		TestResult res = map.get(name);
//		if (res != null) {
//			return res.getOutput();
//		}
//		return null;
//	}
//	
//	public static List<String> getOutput(String testName, String methodName) {
//		TestResult res = map.get(testName);
//		if (res != null) {
//			return res.getOutput(methodName);
//		}
//		return null;
//	}
//
//	/**
//	 * Erase the content of all the output generated so far.
//	 */
//	public static void clear() {
//		m_output.clear();
//	}
//
//	/**
//	 * Log the passed string to the HTML reports. If logToStandardOut is true,
//	 * the string will also be printed on standard out.
//	 *
//	 * @param s
//	 *            The message to log
//	 * @param logToStandardOut
//	 *            Whether to print this string on standard out too
//	 */
//	public static void log(String s, boolean logToStandardOut) {
//		getCurrentTestResult().appendInstruction(s);
//		if (logToStandardOut) {
//			System.out.println(s);
//		}
//
//	}
//
//	public synchronized static void logData() {
//		JSONObject json = new JSONObject(instruction.get());
//		getCurrentTestResult().appendInstruction(json.toJSONString());
//		instruction.get().clear();
//	}
//
//	/**
//	 * Log the passed string to the HTML reports
//	 * 
//	 * @param s
//	 *            The message to log
//	 */
//	public static void log(String key, String value) {
//		instruction.get().put(key, value);
//	}
//
//	private static synchronized void log(String s, TestResult m) {
//		
//		int n = getOutput(m.getName()).size();
//		List<Integer> lines = m_methodOutputMap.get(m);
//		if (lines == null) {
//		      lines = Lists.newArrayList();
//		      m_methodOutputMap.put(m, lines);
//		 }
//		lines.add(n);
//		getOutput(m.getName()).add(s);
//	}

}
