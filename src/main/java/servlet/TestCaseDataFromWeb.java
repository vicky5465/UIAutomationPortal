package servlet;

import java.util.List;

public class TestCaseDataFromWeb {
	private List<String> caseNames;
	private String testingEnv;
	private String testingPlatform;
	private String testingBrowser;
	private String testingUser;
	private String testingPwd;
	
	public TestCaseDataFromWeb(){
		
	}
	
	public TestCaseDataFromWeb(List<String> caseNames, String testingEnv, String testingPlatform, String testingBrowser,
			String testingUser, String testingPwd) {
		super();
		this.caseNames = caseNames;
		this.testingEnv = testingEnv;
		this.testingPlatform = testingPlatform;
		this.testingBrowser = testingBrowser;
		this.testingUser = testingUser;
		this.testingPwd = testingPwd;
	}

	public List<String> getCaseNames() {
		return caseNames;
	}

	public void setCaseNames(List<String> caseNames) {
		this.caseNames = caseNames;
	}

	public String getTestingEnv() {
		return testingEnv;
	}

	public void setTestingEnv(String testingEnv) {
		this.testingEnv = testingEnv;
	}

	public String getTestingPlatform() {
		return testingPlatform;
	}

	public void setTestingPlatform(String testingPlatform) {
		this.testingPlatform = testingPlatform;
	}

	public String getTestingBrowser() {
		return testingBrowser;
	}

	public void setTestingBrowser(String testingBrowser) {
		this.testingBrowser = testingBrowser;
	}

	public String getTestingUser() {
		return testingUser;
	}

	public void setTestingUser(String testingUser) {
		this.testingUser = testingUser;
	}

	public String getTestingPwd() {
		return testingPwd;
	}

	public void setTestingPwd(String testingPwd) {
		this.testingPwd = testingPwd;
	}
	
	
}
