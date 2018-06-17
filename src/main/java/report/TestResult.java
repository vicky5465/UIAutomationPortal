package report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestResult {

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int SKIP = 3;
	public static final int SUCCESS_PERCENTAGE_FAILURE = 4;
	public static final int STARTED = 16;

		private int m_status = -1;
		private Throwable m_throwable = null;
		private long m_startMillis = 0;
		private long m_endMillis = 0;
		private String m_host;
		private String m_name = null;
		private List<String> m_output = null;
		private Map<String, List<String>> instruction_map = null;
		private String currentMethod = null;

		public TestResult(String name, String host, long start, long end, Throwable throwable) {
			m_name = name;
			m_host = host;
			m_startMillis = start;
			m_endMillis = end;
			m_throwable = throwable;
//			instruction_map = Collections.synchronizedMap(new HashMap<>());
//			m_output = Collections.synchronizedList(new ArrayList<>());
		}

		public int getStatus() {
			return m_status;
		}

	
		public void setStatus(int status) {
			m_status = status;
		}

		
		public Throwable getThrowable() {
			return m_throwable;
		}

		
		public long getStartMillis() {
			return m_startMillis;
		}

		
		public long getEndMillis() {
			return m_endMillis;
		}

		
		public void setEndMillis(long millis) {
			m_endMillis = millis;
		}

		
		public String getName() {
			return m_name;
		}

		
		public void setName(String name) {
			m_name = name;
		}

		
		public boolean isSuccess() {
			return SUCCESS == m_status;
		}

		
		public String getHost() {
			return m_host;
		}

		
		public void setHost(String host) {
			m_host = host;
		}

		
		public synchronized List<String> getOutput() {
			return m_output;
		}

		public synchronized void appendInstruction(String i) {
			instruction_map.get(currentMethod).add(i);
			m_output.add(i);
		}

		public String toString() {
			String str = "Name: " + m_name + " Host: " + m_host;
			return str;
		}

		public void setMethod(String method) {
			if (!method.equalsIgnoreCase(currentMethod)) {
				this.currentMethod = method;
//				List<String> instructions = Collections.synchronizedList(new ArrayList<>());
//				instruction_map.put(currentMethod, instructions);
			}

		}

		public List<String> getOutput(String methodName) {
			return instruction_map.get(methodName);
		}
	}