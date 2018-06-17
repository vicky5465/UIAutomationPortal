package servlet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import utils.file.JSONManager;


/**
 * to export the case description to UI
 * @author vicky
 *
 */
@Path("/ExportTestCaseDesc")
public class ExportTestCaseDesc {
	@Context
	private ServletContext s;
	 
	@GET
	@Produces("application/json")
	public Response getTestCaseDescription(@QueryParam("Case") String requestCase) throws JSONException {
		String result="";
//		JSONObject jsonObject = new JSONObject();
		if(requestCase==null){
			result = "please give me the test case name";
			return Response.status(201).entity(result).build();
		}else{
			String file = s.getRealPath("CaseDescMapping.json");
//			System.out.println(file);
			Map<String, Object> descMap = JSONManager.convertJsonDataToMap(file);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("caseDesc", descMap.get(requestCase));
			result = jsonObject.toString()
					;
			}
		return Response.status(200).entity(result).build();
	}

}
