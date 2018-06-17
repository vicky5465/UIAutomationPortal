package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

//import org.bouncycastle.asn1.ocsp.Request;
import org.json.JSONException;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

/**
 * this endpoint is used to get whole test suites
 * @author vicky
 *
 */
@Path("/ExportSuite")
public class ExportSuite {
	@Context 
	HttpServletRequest request;
	@GET
	@Produces("application/json")
	
	public Response getTestID() throws JSONException {
//		List<String> testIDs = new ArrayList<>();
		Set<String> testSuites = new HashSet<String>();
//to get the test suite
		 Reflections reflections = new Reflections("com.movoto.testSuites.AgentApp.*");
		 for (String s : reflections.getStore().get(SubTypesScanner.class).values()) {
			 
			 try {
				Class clazz = Class.forName(s);
//				System.out.println(clazz);
				Package pac = clazz.getPackage();
				String suite = pac.getName();
//				System.out.println(suite);
//				 testIDs.add(s);
				 testSuites.add(suite);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 }
		 
		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("caseId", testIDs);
		jsonObject.put("suite", testSuites);
		
		//to get the request ip:
		String ip = request.getRemoteAddr();
		jsonObject.put("ip", ip);
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

}
