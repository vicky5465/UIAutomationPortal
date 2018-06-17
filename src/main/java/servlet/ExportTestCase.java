package servlet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

@Path("/ExportTestCase")
public class ExportTestCase {
	@GET
	@Produces("application/json")
	public Response getTestID(@QueryParam("Suite") String requestSuite) throws JSONException {
		List<String> testCases = new ArrayList<>();
		List<String> requestTestCases = new ArrayList<>();
		Map<String, String> caseSuiteMap = new HashMap<String, String>();

		
		String result ="";
		Reflections reflections = new Reflections("com.movoto.testSuites.AgentApp.*");
		for (String testCase : reflections.getStore().get(SubTypesScanner.class).values()) {

			try {
				Class clazz = Class.forName(testCase);
//				System.out.println(clazz);
				Package pac = clazz.getPackage();
				String suite = pac.getName();
//				System.out.println(suite);
				testCases.add(testCase);
				caseSuiteMap.put(testCase, suite);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

		if(requestSuite==null){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("teseCase", testCases);
			result = jsonObject.toString();
		}else{
			JSONObject jsonObject = new JSONObject();
			for ( String testCase : caseSuiteMap.keySet() ) {
				if(caseSuiteMap.get(testCase).equals(requestSuite)){
					requestTestCases.add(testCase);
				}
			}
			
			jsonObject.put("teseCase", requestTestCases);
			
			result = jsonObject.toString();
			
		}
		return Response.status(200).entity(result).build();
	}
	
	@Path("/ExportTestMethod")
	@GET
	@Produces("application/json")
	public Response getTestMethod(@QueryParam("Case") String caseName) throws JSONException {
		String result ="";
		List<Method> testMethods = Common.getTestMethod(caseName);
		List<String> methods = new ArrayList<String>();
		for(Method testMethod: testMethods){
			methods.add(testMethod.getName());
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("teseMethods", methods);
		result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}
}
