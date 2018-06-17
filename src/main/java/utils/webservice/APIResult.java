package utils.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

//import com.movoto.domain.Agent;
//import com.movoto.domain.Client;
import utils.file.JSONManager;

import net.minidev.json.JSONArray;

public class APIResult {
	private APIManager api;

	public APIResult() {
		api = new APIManager();
	}

	public int getFilterCount(Map<String, Object> data, int filterIndex) {

		String response = api.getFilterResponse(data,filterIndex);
		int numFromAPI = (int) JSONManager.getValueFromJson("$.filteredCount", response);
		return numFromAPI;
	}




}
